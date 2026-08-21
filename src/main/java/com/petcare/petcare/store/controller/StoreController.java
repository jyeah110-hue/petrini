package com.petcare.petcare.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.petcare.petcare.common.vo.PageVO;
import com.petcare.petcare.store.service.StoreService;
import com.petcare.petcare.store.vo.BrandVO;
import com.petcare.petcare.store.vo.CategoryVO;
import com.petcare.petcare.store.vo.ProductVO;
import com.petcare.petcare.store.vo.SearchVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
    
    @Value("${toss.client-key}")
    private String tossApiKey;

    private final StoreService storeService;

    @GetMapping({"", "/"})
    public String list(SearchVO search, Model model) throws Exception {
        List<ProductVO> productList = storeService.getProductList(search);
        List<CategoryVO> categoryList = storeService.getCategoryList();
        List<BrandVO> brandList = storeService.getBrandList();
        
        int curPage = search.getCurPage() == null ? 1 : search.getCurPage();
        int pageSize = search.getPageSize() == null ? 1 : search.getPageSize();
        int totalCount = productList == null ? 0 : productList.size();
        PageVO paging = new PageVO(curPage, pageSize, totalCount);

        if (search.getSpecies() == null) search.setSpecies(2);
        model.addAttribute("productList", productList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("brandList", brandList);
        model.addAttribute("search", search);
        model.addAttribute("paging", paging);
        return "store/list";
    }

    // ── 상품 상세 ───────────────────────────────────────────
    @GetMapping("/detail")
    public String detail(@RequestParam(defaultValue = "1") String id, Model model) {
        model.addAttribute("id", id);
        return "store/detail";
    }

    // ── 장바구니 ────────────────────────────────────────────
    @GetMapping("/cart")
    public String cart() {
        return "store/cart";
    }

    // ── 주문서 작성 (배송지 입력) ───────────────────────────
    @GetMapping("/order")
    public String order() {
        return "store/order";
    }

    // ── 결제 (결제수단 선택) ────────────────────────────────
    @GetMapping("/payment")
    public String payment(Model model) {
        model.addAttribute("tossApiKey", tossApiKey);
        return "store/payment";
    }

    // 결제 요청 성공 시 여기로 돌아옴 (아직 승인 API는 호출 안 함)
    @GetMapping("/test/payment/success")
    @ResponseBody
    public String success(@RequestParam String orderId,
                           @RequestParam String amount,
                           @RequestParam String paymentKey) {
        return "결제 요청 성공! orderId=" + orderId + ", amount=" + amount + ", paymentKey=" + paymentKey;
    }

    @GetMapping("/test/payment/fail")
    @ResponseBody
    public String fail(@RequestParam(required = false) String code,
                        @RequestParam(required = false) String message) {
        return "결제 요청 실패: " + code + " - " + message;
    }

    // ── 결제 완료 ───────────────────────────────────────────
    @GetMapping("/order-complete")
    public String orderComplete() {
        return "store/order-complete";
    }
}
