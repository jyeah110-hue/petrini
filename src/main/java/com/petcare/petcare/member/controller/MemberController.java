package com.petcare.petcare.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.petcare.petcare.common.api.JavaMailService;
import com.petcare.petcare.common.api.KakaoOAuthService;
import com.petcare.petcare.common.security.MemberUserDetails;
import com.petcare.petcare.member.service.MemberService;
import com.petcare.petcare.member.vo.KakaoUserVO;
import com.petcare.petcare.member.vo.MemberVO;
import com.petcare.petcare.member.vo.SocialUserInfo;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    private final KakaoOAuthService kakaoOAuthService;
    private final JavaMailService javaMailService;

    /*
     * [로그인 페이지 - GET만 남김]
     * POST /login은 Spring Security가 처리한다. (SecurityConfig.loginProcessingUrl)
     * → 기존 @PostMapping("/login") 메서드는 제거
     * → 기존 @GetMapping("/member/logout") 메서드도 제거 (Security가 처리)
     */
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    // @PostMapping("/login")
    // public String login(@RequestParam(required = false) String id,
    //                     @RequestParam(required = false) String password,
    //                     @RequestParam(required = false) String redirect,
    //                     HttpSession session) throws Exception {
                         
    //     MemberVO member = memberService.login(id, password);
    //     if (member == null) {
    //         return "redirect:/login?error=invalid";
    //     }

    //     session.setAttribute("memberInfo", member);

    //     if (redirect != null && !redirect.isBlank() && redirect.startsWith("/") && !redirect.startsWith("//")) {
    //         return "redirect:" + redirect;
    //     }

    //     return "redirect:/";
    // }

    /*
     * [로그아웃 제거]
     * Spring Security의 LogoutFilter가 /member/logout을 처리한다.
     * SecurityConfig에서 .logoutUrl("/member/logout")으로 설정.
     * 아래 메서드는 더 이상 필요 없음.
     */
    // @GetMapping("/member/logout") → Security가 처리
    // @GetMapping("/member/logout")
    // public String logout(HttpSession session) {
    //     session.invalidate();
    //     return "redirect:/";
    // }

    @GetMapping("/oauth/kakao")
    public String kakaoLogin() {
        return "redirect:" + kakaoOAuthService.getAuthorizeUrl();
    }

    @GetMapping("/oauth/kakao/callback")
    public String kakaoCallback(@RequestParam(required = false) String code,
                                @RequestParam(required = false) String error,
                                HttpSession session) throws Exception {

        //사용자가 카카오 로그인을 취소한 경우                            
        if (error != null || code == null || code.isBlank()) {
            return "redirect:/login?error=kakao_cancel";
        }        

        String accessToken = kakaoOAuthService.getAccessToken(code);
        if (accessToken == null) {
            return "redirect:/login?error=kakao_token";
        }
    
        KakaoUserVO kakaoUser = kakaoOAuthService.getUserInfo(accessToken);
        if (kakaoUser == null) {
            return "redirect:/login?error=kakao_user";
        }

        session.setAttribute("socialUserInfo", kakaoUser);

        MemberVO member = memberService.getMemberById(kakaoUser.getKakaoId().toString());    
        if (member == null) {
            return "redirect:/join";                
        }

        /*
         * [SecurityContext에 직접 인증 정보 설정]
         * 카카오 로그인은 Spring Security의 폼 로그인 흐름이 아니므로
         * 수동으로 Authentication을 만들어서 SecurityContext에 넣어야 한다.
         * → 이래야 Security가 이 사용자를 "인증된 사용자"로 인식
         * → 이후 @PreAuthorize, hasRole() 등이 정상 동작
         */
        member.setPassword(null);
        MemberUserDetails userDetails = new MemberUserDetails(member);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,                   //principle 
                                                                                                     null,                          //credentials(비밀번호 - 이미 인증됐으니 null)
                                                                                                     userDetails.getAuthorities()   //권한목록
                                                                                                    );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        session.setAttribute("memberInfo", member);
        return "redirect:/";
    }
    

    @GetMapping("/join")
    public String join() {
        return "member/join";
    }





    //AJAX
    @GetMapping("/join/checkId")
    @ResponseBody
    public Map<String, Object> checkId(@RequestParam String id) throws Exception {
        Map<String, Object> result = memberService.checkId(id);
        return result;
    }

    @GetMapping("/join/checkEmail")
    @ResponseBody
    public Map<String, Object> checkEmail(@RequestParam String email) throws Exception {
        Map<String, Object> result = memberService.checkEmail(email);
        return result;
    }

    @PostMapping("/join/checkEmailverifyCode")
    @ResponseBody
    public Map<String, Object> verifyCode(@RequestParam String email,
                                          @RequestParam String code,
                                          HttpSession session) {

        String savedCode = (String)session.getAttribute("emailVerifyCode");
        String savedEmail = (String)session.getAttribute("emailVerifyEmail");
        Long expireTime = (Long)session.getAttribute("emailVerifyExpire");

        Map<String, Object> result = new HashMap<>();

        // 세션에 인증 정보가 없음
        if (savedCode == null || savedEmail == null || expireTime == null) {
            result.put("available", false);
            result.put("message", "인증번호를 먼저 발송해 주세요.");
            return result;
        }

        // 만료 확인
        if (System.currentTimeMillis() > expireTime) {
            session.removeAttribute("emailVerifyCode");
            session.removeAttribute("emailVerifyEmail");
            session.removeAttribute("emailVerifyExpire");

            result.put("available", false);
            result.put("message", "인증번호가 만료되었습니다. 다시 발송해 주세요.");
            return result;
        }

        // 이메일 일치 확인
        if (!savedEmail.equals(email.trim())) {
            result.put("available", false);
            result.put("message", "인증 요청한 이메일과 다릅니다.");
            return result;
        }

        // 인증번호 비교
        if (!savedCode.equals(code.trim())) {
            result.put("available", false);
            result.put("message", "인증번호가 일치하지 않습니다.");
            return result;
        }

        // 인증 성공 → 세션에 인증 완료 표시
        result.put("available", true);
        result.put("message", "이메일 인증이 완료되었습니다.");
        return result;
    }

    @PostMapping("/join/sendEmailVerifyCode")
    @ResponseBody
    public Map<String, Object> sendCode(@RequestParam String email, HttpSession session) {

        Map<String, Object> result = new HashMap<>();
        
        // 이메일 형식 검증
        if (email == null || !email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            result.put("available", false);
            result.put("message", "올바른 이메일 형식이 아닙니다.");
            return result;
        }

        try {
            String code = javaMailService.generateCode();
            javaMailService.sendVerifyCodeEmail(email.trim(), code);

            // 세션에 인증번호 + 만료시간(5분) 저장
            session.setAttribute("emailVerifyCode", code);
            session.setAttribute("emailVerifyEmail", email.trim());
            session.setAttribute("emailVerifyExpire", System.currentTimeMillis() + (5 * 60 * 1000));

            result.put("available", true);
            result.put("message", "인증번호가 발송되었습니다.");
            return result;
        } catch (Exception e) {
            result.put("available", false);
            result.put("message", "메일 발송 실패: " + e.getMessage());
            return result;
        }
    }

    @PostMapping("/join")
    @ResponseBody
    public String join(@Valid MemberVO member, HttpSession session) throws Exception {
        SocialUserInfo socialUserInfo = (SocialUserInfo)session.getAttribute("socialUserInfo");
        String result = memberService.addMember(member, socialUserInfo);
        if (result != null) {
            return "ERROR:" + result;
        }

        // 세션 정리
        if (socialUserInfo != null) {
            session.removeAttribute("socialUserInfo");
        }    

        return "OK";
    }
}
