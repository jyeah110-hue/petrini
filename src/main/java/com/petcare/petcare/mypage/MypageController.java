package com.petcare.petcare.mypage;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 마이페이지 컨트롤러.
 * 
 * [변경] 각 메서드의 session.getAttribute("memberInfo") == null 체크를 제거.
 * SecurityConfig에서 /mypage/** 는 authenticated()로 설정되어 있으므로
 * 비로그인 사용자가 접근하면 Security가 자동으로 /login으로 리다이렉트.
 */

@Controller 
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    /** 마이홈 (대시보드) */
    @GetMapping({"", "/"})
    public String dashboard(HttpSession session) {
        return "mypage/dashboard";
    }

    /** 주문내역 */
    @GetMapping("/orders")
    public String orders(HttpSession session) {
        return "mypage/orders";
    }

    /** 예약내역 */
    @GetMapping("/reserve")
    public String reserve(HttpSession session) {
        return "mypage/reserve";
    }

    /** 포인트/쿠폰 */
    @GetMapping("/points")
    public String points(HttpSession session) {
        return "mypage/points";
    }

    /** 관심상품 */
    @GetMapping("/wishlist")
    public String wishlist(HttpSession session) {
        return "mypage/wishlist";
    }

    /** 반려동물 관리 */
    @GetMapping("/pets")
    public String pets(HttpSession session) {
        return "mypage/pets";
    }

    /** 건강수첩 */
    @GetMapping("/health")
    public String health(HttpSession session) {
        return "mypage/health";
    }


    /** 알림함 */
    @GetMapping("/notifications")
    public String notifications(HttpSession session) {
        return "mypage/notifications";
    }

    /** 회원정보 수정 */
    @GetMapping("/edit")
    public String edit(HttpSession session) {
        return "mypage/edit";
    }
}
