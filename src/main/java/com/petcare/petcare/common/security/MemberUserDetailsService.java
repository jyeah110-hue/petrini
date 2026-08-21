package com.petcare.petcare.common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.petcare.petcare.member.mapper.MemberMapper;
import com.petcare.petcare.member.vo.MemberVO;

import lombok.RequiredArgsConstructor;

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

 @Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {
    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberVO member;
        try {
            member = memberMapper.selectMemberById(username);
        } 
        catch (Exception e) {
            throw new UsernameNotFoundException("회원 조회 중 오류: " + username, e);
        }

        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 아이디: " + username);
        }

        return new MemberUserDetails(member);
    }

}
