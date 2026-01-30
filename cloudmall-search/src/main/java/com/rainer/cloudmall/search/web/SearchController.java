package com.rainer.cloudmall.search.web;

import com.rainer.cloudmall.search.service.SearchService;
import com.rainer.cloudmall.search.vo.SearchParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/list.html")
    public String listPage(SearchParam param) {
        return "list";
    }
}
