package com.petcare.petcare.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.petcare.petcare.biz.service.BizService;
import com.petcare.petcare.biz.vo.BusinessVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/biz")
public class AdminBizController extends AdminController {
    @Autowired
    BizService bizService;

    // ── ADMIN-03 사업자 승인 ───────────────────────────────
    @GetMapping("/list")
    public String bizList(@RequestParam(defaultValue = "PENDING") String status,
                          Model model,
                          HttpSession session) throws Exception{
        if (getAdmin(session) == null) 
            return "redirect:/login";
        
        List<BusinessVO> list = bizService.getBusinessList(status);
        Map<String, Integer> counts = bizService.getBizStatusCounts();
        model.addAttribute("applyList", list);
        model.addAttribute("status", status);
        model.addAttribute("counts", counts);

        return "admin/biz/list";
    }
    
    @PostMapping("/approve")
    @ResponseBody
    public String approve(@RequestParam String memberId,
                          @RequestParam String action,
                          @RequestParam(required = false) String reason,
                          HttpSession session) throws Exception {

        if (getAdmin(session) == null)
        return "redirect:/login";

        bizService.bizApprove(memberId, action);
        return "OK";
    }

    // ── 재능나눔 승인 ───────────────────────────────
    @GetMapping("/talent")
    public String cmsTalent(HttpSession session) {
        if (getAdmin(session) == null) 
            return "redirect:/login";

        return "admin/biz/talent";
    } 
}
