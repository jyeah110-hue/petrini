package com.petcare.petcare.stay.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.petcare.common.api.KakaoMapService;
import com.petcare.petcare.common.api.KakaoMemoService;
import com.petcare.petcare.common.api.TossPaymentsService;
import com.petcare.petcare.file.service.FileService;
import com.petcare.petcare.file.vo.FileVO;
import com.petcare.petcare.member.vo.MemberVO;
import com.petcare.petcare.member.vo.SocialUserInfo;
import com.petcare.petcare.payment.PaymentService;
import com.petcare.petcare.pet.service.PetService;
import com.petcare.petcare.reserve.service.ReserveService;
import com.petcare.petcare.reserve.vo.ReserveVO;
import com.petcare.petcare.stay.service.StayService;
import com.petcare.petcare.stay.vo.StayVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor
@RequestMapping("/stay")
public class StayController {
    @Value("${toss.client-key}")
    private String tossApiKey;
    
    private final StayService stayService;
    private final PetService petService;
    private final ReserveService reserveService;
    private final FileService fileService;
    private final PaymentService paymentService;

    private final KakaoMapService kakaoMapService;
    private final KakaoMemoService kakaoMemoService;
    private final TossPaymentsService tossPaymentsService;

    @GetMapping({"", "/"})
    public String list(@ModelAttribute("search") StayVO vo, 
                       Model model) throws Exception {
        List<StayVO> stayList = stayService.getStayListBySearch(vo);
        kakaoMapService.addMapAttributes(model, stayList);
        
        model.addAttribute("stayList", stayList);
        model.addAttribute("skipAutoMarkers", "true");
        return "stay/list";
    }

    // ── 숙소 상세 ─────────────────────────────────────
    @GetMapping("/detail")
    public String detail(@RequestParam Long stayId, Model model) throws Exception {
        StayVO stay = stayService.getStay(stayId);
        List<FileVO> imgList = fileService.getFileList("STAY", stayId.toString());

        if (stay != null && stay.getLat() != null) {
            List<StayVO> singleList = new ArrayList<>();
            singleList.add(stay); 
            kakaoMapService.addMapAttributes(model, singleList);
        }

        model.addAttribute("stay", stay);
        model.addAttribute("imgList", imgList);
        return "stay/detail";
    }

    // ── 숙소 예약 ───────────────────────────────────────────
    @GetMapping("/reserve")
    public String reserve(@RequestParam("stayId") Long stayId,
                          @RequestParam(value = "roomId") Long roomId,
                          HttpSession session, 
                          Model model) throws Exception {
        MemberVO member = (MemberVO) session.getAttribute("memberInfo");
        if (member == null) return "redirect:/login";

        StayVO stay = stayService.getStay(stayId);
        if (stay == null) return "redirect:/stay";

        model.addAttribute("stay", stay);
        model.addAttribute("roomId", roomId);
        model.addAttribute("petList", petService.getPetList(member.getId()));
        return "stay/reserve";
    }

    @PostMapping("/reserve")
    public String reserve(@ModelAttribute ReserveVO vo,
                          HttpSession session,
                          RedirectAttributes rttr) throws Exception {

        MemberVO member = (MemberVO) session.getAttribute("memberInfo");
        if (member == null) return "redirect:/login";

        vo.setMemberId(member.getId());

        try {
            //예약하는 순간 PENDING
            String resvId = reserveService.createStayReserve(vo);
            // 결제 페이지로 리다이렉트
            return "redirect:/stay/payment?resvId=" + resvId;
        } catch (RuntimeException e) {
            rttr.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/stay/reserve?stayId=" + vo.getBizId() + "&roomId=" + vo.getRoomId();
        }
    }

    @GetMapping("/payment")
    public String payment(@RequestParam("resvId") String resvId,
                          HttpSession session,
                          Model model) throws Exception {

        MemberVO member = (MemberVO) session.getAttribute("memberInfo");
        if (member == null) return "redirect:/login";

        ReserveVO reserve = reserveService.getStayReserveByResvId(resvId);
        if (reserve == null) return "redirect:/stay";

        // 본인 예약만 결제 가능
        if (!member.getId().equals(reserve.getMemberId())) {
            return "redirect:/stay";
        }

        model.addAttribute("reserve", reserve);
        model.addAttribute("tossApiKey", tossApiKey);
        return "stay/payment";
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam("orderId") String orderId,
                                 @RequestParam("paymentKey") String paymentKey,
                                 @RequestParam("amount") int amount,
                                 HttpSession session,
                                 Model model) throws Exception {

        MemberVO member = (MemberVO) session.getAttribute("memberInfo");
        if (member == null) return "redirect:/login";

        // orderId 형식: stay-{resvId}-{usedPoint}-{timestamp}
        String[] parts = orderId.split("-");
        String resvId = parts[1];
        int usedPoint = parts.length >= 4 ? Integer.parseInt(parts[2]) : 0;

        // 예약 조회
        ReserveVO reserve = reserveService.getStayReserveByResvId(resvId);

        try {
            //결제 먼저
            String response = tossPaymentsService.confirmPayment(paymentKey, orderId, amount);
            String payMethod = new ObjectMapper().readTree(response).path("method").asText();

            //토스 승인 성공 후 DB 저장
            paymentService.confirmPayment(reserve, paymentKey, orderId, payMethod);
            
            //카카오 연동 회원일 경우
            SocialUserInfo socialUserInfo = (SocialUserInfo)session.getAttribute("SocialUserInfo");
            if (socialUserInfo != null && "KAKAO".equals(socialUserInfo.getProvider())) {
                kakaoMemoService.sendStayReserveMessage(socialUserInfo.getAccessToken(), reserve);
            }

            //포인트 갱신
            if (usedPoint > 0) {
                int currentPoint = (member.getPoint() != null) ? member.getPoint() : 0;
                member.setPoint(currentPoint - usedPoint);
                session.setAttribute("memberInfo", member);
            }

            return "redirect:/stay/complete?resvId=" + resvId;
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "redirect:/stay";
        }
    }

    // ── 예약 완료 ───────────────────────────────────────────
    @GetMapping("/complete")
    public String complete() {
        return "stay/complete";
    }






    //AJAX
    @GetMapping("/checkAvailability")
    @ResponseBody
    public Map<String, Object> checkAvailability(@RequestParam("roomId") Long roomId,
                                                 @RequestParam("checkinDate") String checkinDate,
                                                 @RequestParam("checkoutDate") String checkoutDate) throws Exception {

        Map<String, Object> result = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date checkin = sdf.parse(checkinDate);
        Date checkout = sdf.parse(checkoutDate);

        boolean available = stayService.checkRoomAvailability(roomId, checkin, checkout);
        result.put("available", available);

        if (!available) {
            result.put("message", "선택한 날짜에 이미 예약이 있습니다.");
        }
        return result;
    }
}
