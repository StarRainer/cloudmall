package com.rainer.cloudmall.search.constant;

public final class ElasticSearchConstants {
    private ElasticSearchConstants() {

    }

    public static final String PRODUCT_INDEX = "product";

    public static final Integer PRODUCT_SEARCH_PAGE_SIZE = 4;

    public static final String HIGH_LIGHT_PRE_TAG = "<b style='color:red'>";

    public static final String HIGH_LIGHT_POST_TAG = "</b>";
}
