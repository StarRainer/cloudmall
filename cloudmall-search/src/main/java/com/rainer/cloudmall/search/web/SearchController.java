package com.rainer.cloudmall.search.web;

import com.rainer.cloudmall.search.service.SearchService;
import com.rainer.cloudmall.search.vo.SearchParam;
import com.rainer.cloudmall.search.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model) {
        SearchResult result = searchService.search(param);
        model.addAttribute("result", result);
        return "list";
    }
}
