package com.petcare.petcare.stay.controller;

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
import com.petcare.petcare.member.vo.MemberVO;
import com.petcare.petcare.stay.service.StayService;
import com.petcare.petcare.stay.vo.RoomVO;
import com.petcare.petcare.stay.vo.StayVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor
@RequestMapping("/biz/stay")
public class BizStayController extends BizController {

    private final FileService fileService;
    private final StayService stayService;
    
    @GetMapping({"", "/"})
    public String dashboard(HttpSession session) {
        return "biz/stay/dashboard";
    }
    @GetMapping("/reserve")
    public String reserve(HttpSession session) {
        return "biz/stay/reserve";
    }
    
    @GetMapping("/rooms")
    public String rooms(HttpSession session, Model model) throws Exception {
        MemberVO member = getBizMember(session);
        StayVO stay = stayService.getStayByMemberId(member.getId());
        if (stay == null) return "redirect:/mypage/biz";

        List<RoomVO> roomList = stayService.getRoomListByStayId(stay.getStayId());
        model.addAttribute("roomList", roomList);

        return "biz/stay/rooms";
    }

    @PostMapping("/rooms")
    public String saveRoom(RoomVO room,
                           HttpSession session,
                           RedirectAttributes rttr) throws Exception {
        MemberVO member = getBizMember(session);

        StayVO stay = stayService.getStayByMemberId(member.getId());
        if (stay == null) return "redirect:/mypage/biz";

        room.setStayId(stay.getStayId());

        if (room.getRoomId() == null) {
            stayService.insertRoom(room);
            rttr.addFlashAttribute("msg", "객실이 등록되었습니다.");
        } else {
            // 수정
            stayService.updateRoom(room);
            rttr.addFlashAttribute("msg", "객실 정보가 수정되었습니다.");
        }
        return "redirect:/biz/stay/rooms";
    }

    @PostMapping("/rooms/delete")
    public String deleteRoom(@RequestParam Long roomId,
                             HttpSession session,
                             RedirectAttributes rttr) throws Exception {
        MemberVO member = getBizMember(session);
        StayVO stay = stayService.getStayByMemberId(member.getId());
        if (stay == null) return "redirect:/mypage/biz";

        stayService.deleteRoom(roomId, stay.getStayId());

        rttr.addFlashAttribute("msg", "객실이 삭제되었습니다.");
        return "redirect:/biz/stay/rooms";
    }

    @GetMapping("/reviews")
    public String reviews(HttpSession session) {
        return "biz/stay/reviews";
    }
    @GetMapping("/settlement")
    public String settlement(HttpSession session) {
        return "biz/stay/settlement";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) throws Exception {
        MemberVO member = getBizMember(session);
        StayVO stay = stayService.getStayByMemberId(member.getId());
        if (stay == null) {
            return "redirect:/mypage/biz";
        }
        
        List<FileVO> imgList = fileService.getFileList("STAY", stay.getStayId().toString());
        
        model.addAttribute("stay", stay);
        model.addAttribute("imgList", imgList);

        return "biz/stay/profile";
    }

    @PostMapping("/profile")
    public String profile(StayVO vo,
                          @RequestParam(value = "tags", required = false) String[] tags,
                          @RequestParam(value = "imgs", required = false) MultipartFile[] imgs,
                          @RequestParam(value = "deleteFileIds", required = false) Long[] deleteFileIds,
                          HttpSession session,
                          RedirectAttributes rttr) throws Exception {

        MemberVO member = getBizMember(session);
        StayVO stay = stayService.getStayByMemberId(member.getId());
        if (stay == null) {
            return "redirect:/mypage/biz";
        }
        vo.setStayId(stay.getStayId());

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
                fileService.uploadFile(img, "STAY", vo.getStayId().toString());
            }
        }

        vo.setAddr1(stay.getAddr1());
        vo.setAddr2(stay.getAddr2());
        stayService.updateStay(vo);

        rttr.addFlashAttribute("msg", "저장되었습니다.");
        return "redirect:/biz/stay/profile";
    }
    
    @GetMapping("/info")
    public String info(HttpSession session, Model model) throws Exception {
        MemberVO member = getBizMember(session);
        StayVO stay = stayService.getStayByMemberId(member.getId());
        model.addAttribute("stay", stay);; 

        return "biz/stay/info";
    }    
}
