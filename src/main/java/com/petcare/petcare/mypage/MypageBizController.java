package com.petcare.petcare.mypage;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.petcare.biz.service.BizService;
import com.petcare.petcare.biz.vo.BusinessVO;
import com.petcare.petcare.common.util.HttpUtils;
import com.petcare.petcare.member.vo.MemberVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor
@RequestMapping("/mypage/biz")
public class MypageBizController {

    @Value("${public.service-api-key}")
    private String publicServiceApiKey;

    private final BizService bizService;

    /** 사업자센터 (BIZ 전용) */
    @GetMapping({"", "/"})
    public String biz(HttpSession session) throws Exception {
        MemberVO memberInfo = (MemberVO) session.getAttribute("memberInfo");
        if (memberInfo == null)
            return "redirect:/login";
        BusinessVO biz = bizService.getBusinessByMemberId(memberInfo.getId());
        if (biz == null || biz.getApproved() == null) {
            return "redirect:/mypage/biz/apply";
        }

        String url = "";
        switch (biz.getType()) {
        case "HOSPITAL":
            url = "redirect:/biz/hospital";
            break;
        case "STAY":
            url = "redirect:/biz/stay";
            break;
        case "RESTAURANT":
            url = "redirect:/biz/restaurant";
            break;
        case "GROOMING":
            url = "redirect:/biz/grooming";
            break;
        case "STUDIO":
            url = "redirect:/biz/studio";
            break;      
        case "STORE":
            url = "redirect:/biz/store";
            break;  
        default:
            url = "mypage/biz";
            break;
        }

        return url;
    }

    /* 사업자 등록 신청 — 폼 페이지 */
    @GetMapping("/apply")
    public String bizApply(HttpSession session,
                            RedirectAttributes redirectAttr) throws Exception {

        MemberVO member = (MemberVO) session.getAttribute("memberInfo");
        if (member == null)
            return "redirect:/login";

        // 기존 신청 이력 확인
        BusinessVO biz = bizService.getBusinessByMemberId(member.getId());
        if (biz != null) {
        if ("APPROVED".equals(biz.getStatus())) {
            // 승인 완료
            return "redirect:/mypage/biz";
        }
        // PENDING
        redirectAttr.addFlashAttribute("name", biz.getName());
        redirectAttr.addFlashAttribute("type", biz.getType());
        redirectAttr.addFlashAttribute("bizNo", biz.getBizNo());
        return "redirect:/mypage/biz/complete";
    }

        return "mypage/biz/apply";
    }

    /* 사업자 등록 신청 — 폼 제출 처리 */
    @PostMapping("/apply")
    public String bizApplySubmit(@RequestParam(required = true) MultipartFile docFile,
                                 @RequestParam(required = false) MultipartFile licenseFile,
                                 BusinessVO vo,
                                 RedirectAttributes redirectAttr,
                                 HttpSession session) throws Exception {

        MemberVO member = (MemberVO) session.getAttribute("memberInfo");

        if (member == null)
            return "redirect:/login";

        try {
            vo.setMemberId(member.getId());
            vo.setStatus("PENDING");
            
            // 파일도 함께 Service에 전달 — 나머지는 Service가 처리
            bizService.applyBusiness(vo, docFile, licenseFile);

            redirectAttr.addFlashAttribute("name", vo.getName());
            redirectAttr.addFlashAttribute("type", vo.getType());
            redirectAttr.addFlashAttribute("bizNo", vo.getBizNo());
            return "redirect:/mypage/biz/complete";
        } 
        catch (Exception e) {
            redirectAttr.addFlashAttribute("errorMsg", "신청 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/mypage/biz/apply";
        }
    }

    /* 신청 완료 페이지 */
    @GetMapping("/complete")
    public String bizComplete(HttpSession session) throws Exception {
        if (session.getAttribute("memberInfo") == null)
            return "redirect:/login";

        return "mypage/biz/complete";
    }






    //AJAX
    /* 사업자등록번호 확인 — 폼 제출 처리 */
    @PostMapping("/checkBizNo")
    @ResponseBody
    public Map<String, Object> checkBizNo(HttpSession session,
                                          @RequestParam String bizNo) throws Exception {

        Map<String, Object> result = new HashMap<>();

        try {
            String url = "https://api.odcloud.kr/api/nts-businessman/v1/status";
            StringBuilder sb = new StringBuilder(url);
            sb.append("?serviceKey=").append(URLEncoder.encode(publicServiceApiKey, "UTF-8"));
            
            String body = "{\"b_no\":[\"" + bizNo.replace("-", "") + "\"]}";
            String response = HttpUtils.request(sb.toString(), "GET", null, body);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode data = root.path("data");

            if (data.isArray() && data.size() > 0) {
                String status = data.get(0).path("b_stt").asText("");
                // "계속사업자" = 정상 / "휴업자" = 휴업 / "폐업자" = 폐업
                if ("계속사업자".equals(status)) {
                    result.put("success", true);
                    result.put("message", "인증 완료 (계속사업자)");
                } else if (status.isEmpty()) {
                    result.put("success", false);
                    result.put("message", "등록되지 않은 사업자등록번호입니다.");
                } else {
                    result.put("success", false);
                    result.put("message", "사업 상태: " + status);
                }
            } 
            else {
                result.put("success", false);
                result.put("message", "조회 결과가 없습니다.");
            }
        } 
        catch (Exception e) {
            result.put("success", false);
            result.put("message", "API 호출 오류: " + e.getMessage());
        }
        return result;
    }
}
