package com.rainer.cloudmall.cart.web;

import com.rainer.cloudmall.cart.service.CartService;
import com.rainer.cloudmall.cart.vo.CartItemVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart.html")
    public String cartList() {
        return "cartList";
    }

    @GetMapping("/addCartItem")
    public String addCartItem(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            Model model) {
        CartItemVo cartItemVo = cartService.addCartItem(skuId, num);
        model.addAttribute("item", cartItemVo);
        return "success";
    }
}
