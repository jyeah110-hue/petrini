package com.petcare.petcare.common.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.petcare.petcare.member.vo.MemberVO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Spring Security의 인증 객체.
 * 
 * [왜 필요한가?]
 * Spring Security는 로그인 성공 시 SecurityContext에 Authentication 객체를 저장하는데,
 * 그 안에 들어가는 주체(principal)가 UserDetails 인터페이스를 구현해야 한다.
 * 
 * 기존 MemberVO를 직접 수정하지 않고, 감싸는(wrapper) 방식으로 구현.
 * → 기존 코드에서 MemberVO를 쓰는 곳은 getMember()로 꺼내 쓸 수 있다.
 * 
 * [role → authority 매핑]
 * DB의 role 컬럼값(USER, BIZ, ADMIN)에 "ROLE_" 접두사를 붙여
 * Spring Security 권한으로 변환한다.
 * → SecurityConfig에서 hasRole("ADMIN") 이렇게 쓸 수 있음
 *    (hasRole은 자동으로 "ROLE_" 접두사를 붙여서 비교)
 */

@Getter @Setter
@RequiredArgsConstructor
public class MemberUserDetails implements UserDetails {
    
    private final MemberVO member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = this.member.getRole();
        if (role == null || role.isBlank())
            role = "USER";

        SimpleGrantedAuthority roles = new SimpleGrantedAuthority("ROLE_" + role);
        List<SimpleGrantedAuthority> roleList = Collections.singletonList(roles);
        return roleList;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getId();
    }
}
