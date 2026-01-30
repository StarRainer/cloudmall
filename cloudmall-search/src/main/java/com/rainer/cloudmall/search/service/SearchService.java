package com.rainer.cloudmall.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.rainer.cloudmall.search.vo.SearchParam;
import com.rainer.cloudmall.search.vo.SearchResult;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    private final ElasticsearchClient elasticsearchClient;

    public SearchService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public SearchResult search(SearchParam param) {

        return null;
    }
}
