package com.rainer.cloudmall.product.web;

import com.rainer.cloudmall.product.service.SkuManager;
import com.rainer.cloudmall.product.vo.SkuItemVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {

    private final SkuManager skuManager;

    public ItemController(SkuManager skuManager) {
        this.skuManager = skuManager;
    }

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) {
        SkuItemVo skuItemVo = skuManager.getItemDetail(skuId);
        model.addAttribute("item", skuItemVo);
        return "item";
    }
}
