package com.petcare.petcare.common.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.petcare.petcare.common.util.LoginUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 폼 로그인 성공 시 실행되는 핸들러.
 * 
 * [왜 필요한가?]
 * 기존 JSP들이 세션에서 ${memberInfo}로 회원 정보에 접근하고 있다.
 * Spring Security는 로그인 성공 시 SecurityContext에 Authentication을 저장하지만,
 * 기존처럼 session.getAttribute("memberInfo")로 접근하려면
 * 별도로 세션에 MemberVO를 넣어줘야 한다.
 * 
 * [동작 흐름]
 * 1. Spring Security가 폼 로그인 인증 성공
 * 2. 이 핸들러의 onAuthenticationSuccess() 호출
 * 3. Authentication에서 MemberUserDetails를 꺼냄
 * 4. MemberUserDetails에서 MemberVO를 꺼내 비밀번호를 지움
 * 5. 세션에 "memberInfo"라는 이름으로 저장
 * 6. redirect 파라미터가 있으면 그 URL로, 없으면 /로 리다이렉트
 */

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Authentication의 principal = MemberUserDetails (loadUserByUsername에서 반환한 객체)
        MemberUserDetails userDetails = (MemberUserDetails)authentication.getPrincipal();

        // 성공 시 로그인 실패 기록 초기화
        LoginUtils.resetAttempts(userDetails.getMember().getId());

        // 비밀번호를 세션에 남기지 않도록 null 처리
        userDetails.getMember().setPassword(null);
        // 기존 JSP 호환: session.getAttribute("memberInfo")로 접근 가능하도록
        request.getSession().setAttribute("memberInfo", userDetails.getMember());
        
        // 로그인 전 페이지로 복귀 (redirect 파라미터가 있을 경우)
        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.isBlank() &&
            redirect.startsWith("/") &&
            !redirect.startsWith("//")) {
            response.sendRedirect(request.getContextPath() + redirect);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/");
    }
}
