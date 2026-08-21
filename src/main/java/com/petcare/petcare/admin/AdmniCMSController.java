package com.petcare.petcare.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/cms")
public class AdmniCMSController extends AdminController {
    // ── ADMIN-03 CMS ───────────────────────────────────────
    @GetMapping("/banner")
    public String cmsBanner(HttpSession session) {
        if (getAdmin(session) == null) 
            return "redirect:/login";

        return "admin/cms/banner";
    }

    @GetMapping("/notice")
    public String cmsNotice(HttpSession session) {
        if (getAdmin(session) == null) 
            return "redirect:/login";

        return "admin/cms/notice";
    }

    @GetMapping("/faq")
    public String cmsFaq(HttpSession session) {
        if (getAdmin(session) == null) 
            return "redirect:/login";

        return "admin/cms/faq";
    }     
}
