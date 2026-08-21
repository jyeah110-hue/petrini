package com.petcare.petcare.common.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.petcare.petcare.common.util.LoginUtils;
import com.petcare.petcare.member.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String memberId = request.getParameter("username");
        LoginUtils.recordFailure(memberId);
        int remaining = LoginUtils.getRemainingAttempts(memberId);

        if (remaining <= 0) {
            response.sendRedirect("/login?error=locked&minutes=30");
        } else {
            response.sendRedirect("/login?error=invalid&remaining=" + remaining);
        }
    }
}
