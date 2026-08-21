package com.petcare.petcare.hospital.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petcare.petcare.biz.controller.BizController;
import com.petcare.petcare.file.service.FileService;
import com.petcare.petcare.file.vo.FileVO;
import com.petcare.petcare.hospital.service.HospitalService;
import com.petcare.petcare.hospital.vo.HospitalVO;
import com.petcare.petcare.member.vo.MemberVO;
import com.petcare.petcare.reserve.service.ReserveService;
import com.petcare.petcare.reserve.vo.ReserveVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor
@RequestMapping("/biz/hospital")
public class BizHospitalController extends BizController {
    
    private final FileService fileService;
    private final HospitalService hospitalService;
    private final ReserveService reserveService;

    @GetMapping({"", "/"})
    public String hospitalDashboard(HttpSession session, Model model) throws Exception {

        MemberVO member = getBizMember(session);
        HospitalVO hospital = hospitalService.getHospitalByMemberId(member.getId());
        if (hospital == null) {
            return "redirect:/mypage/biz";
        }

        model.addAttribute("hospital", hospital);

        return "biz/hospital/dashboard";
    }

    @GetMapping("/reserve")
    public String reserve(@RequestParam(value = "from", required = false) String fromDate,
                          @RequestParam(value = "to", required = false) String toDate,
                          HttpSession session,
                          Model model) throws Exception {
        MemberVO member = getBizMember(session);
        HospitalVO hospital = hospitalService.getHospitalByMemberId(member.getId());
        if (hospital == null) {
            return "redirect:/mypage/biz";
        }

        if (fromDate == null || fromDate.isBlank()) {
            fromDate = java.time.LocalDate.now().minusMonths(3).toString();
        }
        if (toDate == null || toDate.isBlank()) {
            toDate = java.time.LocalDate.now().plusMonths(6).toString();
        }

        List<ReserveVO> reserveList = reserveService.getReserveList(hospital.getBizId(), fromDate, toDate);
        model.addAttribute("hospital", hospital);
        model.addAttribute("reserveList", reserveList);

        return "biz/hospital/reserve";
    }

    @GetMapping("/records")
    public String hospitalRecords(HttpSession session) {
        return "biz/hospital/records";
    }

    @GetMapping("/talent")
    public String hospitalTalent(HttpSession session) {
        return "biz/hospital/talent";
    }

    @GetMapping("/reviews")
    public String hospitalReviews(HttpSession session) {
        return "biz/hospital/reviews";
    }

    @GetMapping("/settlement")
    public String hospitalSettlement(HttpSession session) {
        return "biz/hospital/settlement";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) throws Exception {
        MemberVO member = getBizMember(session);
        HospitalVO hospital = hospitalService.getHospitalByMemberId(member.getId());
        if (hospital == null) {
            return "redirect:/mypage/biz";
        }
        
        List<FileVO> imgList = fileService.getFileList("HOSPITAL", hospital.getHospitalId().toString());
        
        model.addAttribute("hospital", hospital);
        model.addAttribute("imgList", imgList);

        return "biz/hospital/profile";
    }

    @PostMapping("/profile")
    public String saveProfile(HospitalVO vo,
                              @RequestParam(value = "tags", required = false) String[] tags,
                              @RequestParam(value = "imgs", required = false) MultipartFile[] imgs,
                              @RequestParam(value = "deleteFileIds", required = false) Long[] deleteFileIds,
                              HttpSession session,
                              RedirectAttributes rttr) throws Exception {

        MemberVO member = getBizMember(session);
        HospitalVO hospital = hospitalService.getHospitalByMemberId(member.getId());
        if (hospital == null) {
            return "redirect:/mypage/biz";
        }
        vo.setHospitalId(hospital.getHospitalId());

        // 체크 없으면 빈 문자열로 저장 (미선택 = 태그 없음)
        vo.setTags(tags != null ? String.join(",", tags) : "");
        if (deleteFileIds != null) {
            for (Long fileId : deleteFileIds) {
                fileService.deleteFile(fileId);
            }
        }

        if (imgs != null) {
            for (MultipartFile img : imgs) {
                if (img == null || img.isEmpty()) {
                    continue;
                }
                fileService.uploadFile(img, "HOSPITAL", vo.getHospitalId().toString());
            }
        }

        vo.setAddr1(hospital.getAddr1());
        vo.setAddr2(hospital.getAddr2());
        hospitalService.updateHospital(vo);

        rttr.addFlashAttribute("msg", "저장되었습니다.");
        return "redirect:/biz/hospital/profile";
    }
    
    @GetMapping("/info")
    public String info(HttpSession session, Model model) throws Exception {
        MemberVO member = getBizMember(session);
        HospitalVO hospital = hospitalService.getHospitalByMemberId(member.getId());
        model.addAttribute("hospital", hospital);

        return "biz/hospital/info";
    }
}
