package com.petcare.petcare.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.petcare.petcare.biz.controller.BizController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/biz/store")
public class BizStoreController extends BizController {

    // ── 쇼핑몰 (store) ─────────────────────────────────────────
    @GetMapping({"", "/"})
    public String storeDashboard(HttpSession session) {
        return "biz/store/dashboard";
    }

    @GetMapping("/products")
    public String storeProducts(HttpSession session) {
        return "biz/store/products";
    }

    @GetMapping("/inventory")
    public String storeInventory(HttpSession session) {
        return "biz/store/inventory";
    }

    @GetMapping("/orders")
    public String storeOrders(HttpSession session) {
        return "biz/store/orders";
    }

    @GetMapping("/delivery")
    public String storeDelivery(HttpSession session) {
        return "biz/store/delivery";
    }

    @GetMapping("/reviews")
    public String storeReviews(HttpSession session) {
        return "biz/store/reviews";
    }

    @GetMapping("/settlement")
    public String storeSettlement(HttpSession session) {
        return "biz/store/settlement";
    }

    @GetMapping("/info")
    public String storeInfo(HttpSession session) {
        return "biz/store/info";
    }    
}
