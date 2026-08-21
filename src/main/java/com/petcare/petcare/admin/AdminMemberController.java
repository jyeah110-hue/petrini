package com.petcare.petcare.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/member")
public class AdminMemberController extends AdminController {
    
    // ── ADMIN-02 회원 관리 ─────────────────────────────────
    @GetMapping("/list")
    public String memberList(HttpSession session) {
        if (getAdmin(session) == null) 
            return "redirect:/login";

        return "admin/member/list";
    }    
}
