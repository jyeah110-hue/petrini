package com.petcare.petcare.biz.controller;

import com.petcare.petcare.member.vo.MemberVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/biz")
public class BizController {

    // ── 로그인 + BIZ 역할 체크 헬퍼 ──────────────────────────
    public MemberVO getBizMember(HttpSession session) {
        MemberVO m = (MemberVO) session.getAttribute("memberInfo");
        if (m == null || !"BIZ".equals(m.getRole())) return null;
        return m;
    }
}
