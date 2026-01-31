package com.rainer.cloudmall.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.rainer.cloudmall.common.to.es.SkuEsModel;
import com.rainer.cloudmall.search.constant.ElasticSearchConstants;
import com.rainer.cloudmall.search.exception.ElasticSearchQueryException;
import com.rainer.cloudmall.search.utils.PrintUtils;
import com.rainer.cloudmall.search.vo.SearchParam;
import com.rainer.cloudmall.search.vo.SearchResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

@Service
public class SearchService {
    private final ElasticsearchClient elasticsearchClient;
    private final PrintUtils printUtils;

    public SearchService(ElasticsearchClient elasticsearchClient, PrintUtils printUtils) {
        this.elasticsearchClient = elasticsearchClient;
        this.printUtils = printUtils;
    }

    public SearchResult search(SearchParam param) {
        SearchRequest.Builder requestBuilder = new SearchRequest.Builder();
        requestBuilder.index(ElasticSearchConstants.PRODUCT_INDEX);

        buildSearchRequestQueryPart(param, requestBuilder);

        buildSearchRequestOtherPart(param, requestBuilder);

        buildSearchRequestAggregationPart(requestBuilder);

        SearchRequest searchRequest = requestBuilder.build();

        printUtils.printDSL(searchRequest);

        try {
            SearchResponse<SkuEsModel> searchResponse = elasticsearchClient.search(searchRequest, SkuEsModel.class);
            return buildSearchResult(searchResponse, param);
        } catch (IOException e) {
            throw new ElasticSearchQueryException();
        }
    }

    private SearchResult buildSearchResult(SearchResponse<SkuEsModel> searchResponse, SearchParam param) {
        SearchResult searchResult = new SearchResult();
        List<SkuEsModel> esModels = new ArrayList<>();
        if (!CollectionUtils.isEmpty(searchResponse.hits().hits())) {
            for (Hit<SkuEsModel> hit : searchResponse.hits().hits()) {
                SkuEsModel esModel = hit.source();
                if (esModel == null) {
                    continue;
                }
                if (hit.highlight() != null && hit.highlight().containsKey("skuTitle")) {
                    esModel.setSkuTitle(hit.highlight().get("skuTitle").getFirst());
                }
                esModels.add(esModel);
            }
        }
        searchResult.setProducts(esModels);

        buildSearchResultAggregationPart(searchResponse, searchResult);

        searchResult.setPageNum(param.getPageNum());

        if (searchResponse.hits().total() != null) {
            searchResult.setTotal(searchResponse.hits().total().value());
            searchResult.setTotalPages((int) ((searchResult.getTotal() + ElasticSearchConstants.PRODUCT_SEARCH_PAGE_SIZE - 1) / ElasticSearchConstants.PRODUCT_SEARCH_PAGE_SIZE));
            List<Integer> pageNavs = new ArrayList<>();
            for (int i = 1; i <= searchResult.getTotalPages(); i++) {
                pageNavs.add(i);
            }
            searchResult.setPageNavs(pageNavs);
        } else {
            searchResult.setTotal(0L);
            searchResult.setTotalPages(0);
            searchResult.setPageNavs(Collections.emptyList());
        }

        return searchResult;
    }

    private void buildSearchResultAggregationPart(SearchResponse<SkuEsModel> searchResponse, SearchResult searchResult) {
        if (searchResponse.aggregations() != null) {
            Aggregate brandAgg = searchResponse.aggregations().get("brand_agg");
            if (brandAgg != null) {
                searchResult.setBrands(brandAgg.lterms().buckets().array()
                        .stream()
                        .map(longTermsBucket -> {
                            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
                            brandVo.setBrandId(longTermsBucket.key());
                            brandVo.setBrandName(longTermsBucket
                                    .aggregations()
                                    .get("brand_name_agg")
                                    .sterms()
                                    .buckets()
                                    .array()
                                    .getFirst()
                                    .key()
                                    .stringValue()
                            );
                            brandVo.setBrandImg(longTermsBucket
                                    .aggregations()
                                    .get("brand_img_agg")
                                    .sterms()
                                    .buckets()
                                    .array()
                                    .getFirst()
                                    .key()
                                    .stringValue()
                            );
                            return brandVo;
                        })
                        .toList()
                );
            }

            Aggregate attrAgg = searchResponse.aggregations().get("attr_agg");
            if (attrAgg != null) {
                searchResult.setAttrs(attrAgg.nested()
                        .aggregations()
                        .get("attr_id_agg")
                        .lterms()
                        .buckets()
                        .array()
                        .stream()
                        .map(longTermsBucket -> {
                            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
                            attrVo.setAttrId(longTermsBucket.key());
                            attrVo.setAttrName(longTermsBucket
                                    .aggregations()
                                    .get("attr_name_agg")
                                    .sterms()
                                    .buckets()
                                    .array()
                                    .getFirst()
                                    .key()
                                    .stringValue()
                            );
                            attrVo.setAttrValue(longTermsBucket
                                    .aggregations()
                                    .get("attr_value_agg")
                                    .sterms()
                                    .buckets()
                                    .array()
                                    .stream()
                                    .map(stringTermsBucket -> stringTermsBucket.key().stringValue())
                                    .toList()
                            );
                            return attrVo;
                        })
                        .toList()
                );
            }

            Aggregate catalogAgg = searchResponse.aggregations().get("catalog_agg");
            if (catalogAgg != null) {
                searchResult.setCatalogs(catalogAgg
                        .lterms()
                        .buckets()
                        .array()
                        .stream()
                        .map(longTermsBucket -> {
                            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
                            catalogVo.setCatalogId(longTermsBucket.key());
                            catalogVo.setCatalogName(longTermsBucket
                                    .aggregations()
                                    .get("catalog_name_agg")
                                    .sterms()
                                    .buckets()
                                    .array()
                                    .getFirst()
                                    .key()
                                    .stringValue()
                            );
                            return catalogVo;
                        })
                        .toList()
                );
            }
        }
    }

    private void buildSearchRequestAggregationPart(SearchRequest.Builder requestBuilder) {
        Map<String, Aggregation> aggregationMap = new HashMap<>();

        aggregationMap.put("brand_agg", Aggregation.of(aggregation -> aggregation
                .terms(term -> term
                        .field("brandId")
                        .size(20)
                )
                .aggregations("brand_name_agg", Aggregation.of(a -> a
                        .terms(term -> term
                                .field("brandName")
                                .size(1)
                        )
                ))
                .aggregations("brand_img_agg", Aggregation.of(a -> a
                        .terms(term -> term
                                .field("brandImg")
                                .size(1)
                        )
                ))
        ));

        aggregationMap.put("catalog_agg", Aggregation.of(aggregation -> aggregation
                .terms(term -> term
                        .field("catalogId")
                        .size(500)
                )
                .aggregations("catalog_name_agg", Aggregation.of(a -> a
                        .terms(term -> term
                                .field("catalogName")
                                .size(1)
                        )
                ))
        ));

        aggregationMap.put("attr_agg", Aggregation.of(aggregation -> aggregation
                .nested(nest -> nest.path("attrs"))
                .aggregations("attr_id_agg", Aggregation.of(a -> a
                        .terms(term -> term
                                .field("attrs.attrId")
                                .size(500)
                        )
                        .aggregations("attr_name_agg", Aggregation.of(aa -> aa
                                .terms(term -> term
                                        .field("attrs.attrName")
                                        .size(1)
                                )
                        ))
                        .aggregations("attr_value_agg", Aggregation.of(aa -> aa
                                .terms(term -> term
                                        .field("attrs.attrValue")
                                        .size(50)
                                )
                        ))
                ))
        ));

        requestBuilder.aggregations(aggregationMap);
    }

    private void buildSearchRequestOtherPart(SearchParam param, SearchRequest.Builder requestBuilder) {
        if (StringUtils.hasText(param.getSort())) {
            String[] split = param.getSort().split("_");
            requestBuilder.sort(sort -> sort.field(field -> field
                    .field(split[0])
                    .order("asc".equalsIgnoreCase(split[1]) ? SortOrder.Asc : SortOrder.Desc)
            ));
        }

        if (param.getPageNum() != null) {
            requestBuilder.from((param.getPageNum() - 1) * ElasticSearchConstants.PRODUCT_SEARCH_PAGE_SIZE);
        }

        requestBuilder.size(ElasticSearchConstants.PRODUCT_SEARCH_PAGE_SIZE);

        if (StringUtils.hasText(param.getKeyword())) {
            requestBuilder.highlight(highlight -> highlight
                    .fields("skuTitle", field -> field)
                    .preTags(ElasticSearchConstants.HIGH_LIGHT_PRE_TAG)
                    .postTags(ElasticSearchConstants.HIGH_LIGHT_POST_TAG)
            );
        }
    }

    private void buildSearchRequestQueryPart(SearchParam param, SearchRequest.Builder requestBuilder) {
        BoolQuery.Builder boolBuilder = QueryBuilders.bool();
        if (StringUtils.hasText(param.getKeyword())) {
            boolBuilder.must(must -> must.match(match -> match
                    .field("skuTitle")
                    .query(param.getKeyword())
            ));
        }

        if (param.getCatalog3Id() != null) {
            boolBuilder.filter(filter -> filter.term(term -> term
                    .field("catalogId")
                    .value(param.getCatalog3Id())
            ));
        }

        if (!CollectionUtils.isEmpty(param.getBrandId())) {
            List<FieldValue> fieldValues = param.getBrandId().stream().map(FieldValue::of).toList();
            boolBuilder.filter(filter -> filter.terms(terms -> terms
                    .field("brandId")
                    .terms(ts -> ts.value(fieldValues))
            ));
        }

        if (!CollectionUtils.isEmpty(param.getAttrs())) {
            for (String attr : param.getAttrs()) {
                String[] split = attr.split("_");
                long attrId = Long.parseLong(split[0]);
                List<FieldValue> attrValues = Stream.of(split[1].split(":")).map(FieldValue::of).toList();
                boolBuilder.filter(filter -> filter.nested(nested -> nested
                    .path("attrs")
                    .query(query -> query.bool(bool -> bool
                        .filter(f -> f.term(term -> term
                            .field("attrs.attrId")
                            .value(attrId)
                        ))
                        .filter(f -> f.terms(terms -> terms
                            .field("attrs.attrValue")
                            .terms(t -> t
                                .value(attrValues)
                            )
                        ))
                    ))
                ));
            }
        }

        if (param.getHasStock() != null) {
            boolBuilder.filter(filter -> filter.term(term -> term
                    .field("hasStock")
                    .value(param.getHasStock() == 1)
            ));

        }

        if (StringUtils.hasText(param.getSkuPrice())) {
            String[] split = param.getSkuPrice().split("_");
            BigDecimal minPrice = BigDecimal.ZERO;
            BigDecimal maxPrice = BigDecimal.valueOf(Double.MAX_VALUE);
            if (param.getSkuPrice().startsWith("_")) {
                maxPrice = new BigDecimal(split[1]);
            } else if (param.getSkuPrice().endsWith("_")) {
                minPrice = new BigDecimal(split[0]);
            } else if (split.length == 2) {
                minPrice = new BigDecimal(split[0]);
                maxPrice = new BigDecimal(split[1]);
            }
            BigDecimal finalMinPrice = minPrice;
            BigDecimal finalMaxPrice = maxPrice;
            boolBuilder.filter(filter -> filter.range(range -> range
                    .field("skuPrice")
                    .gte(JsonData.of(finalMinPrice))
                    .lte(JsonData.of(finalMaxPrice))
            ));
        }

        requestBuilder.query(query -> query.bool(boolBuilder.build()));
    }
}
