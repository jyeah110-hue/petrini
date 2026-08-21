package com.petcare.petcare.member.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petcare.petcare.member.mapper.MemberMapper;
import com.petcare.petcare.member.vo.MemberSocialVO;
import com.petcare.petcare.member.vo.MemberVO;
import com.petcare.petcare.member.vo.SocialUserInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    
    /*
    * [변경] 기존: private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    * → SecurityConfig에서 @Bean으로 등록한 PasswordEncoder를 주입받는다.
    * → Spring Security가 로그인 시 비밀번호를 비교할 때도 같은 인코더를 사용
    */    
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;

    @Transactional(readOnly = true)
    public Map<String, Object> checkId(String id) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String trimmed = id.trim();
        if (memberMapper.countMemberById(trimmed) > 0) {
            result.put("available", false);
            result.put("message", "이미 사용 중인 아이디입니다.");
            return result;
        }

        result.put("available", true);
        result.put("message", "사용 가능한 아이디입니다.");
        return result;
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> checkEmail(String email) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String trimmed = email.trim();
        if (memberMapper.countMemberByEmail(trimmed) > 0) {
            result.put("available", false);
            result.put("message", "이미 사용 중인 이메일입니다.");
            return result;
        }

        result.put("available", true);
        result.put("message", "사용 가능한 이메일입니다.");
        return result;
    }

    public MemberVO getMemberById(String id) throws Exception {
        return memberMapper.selectMemberById(id);
    }

    @Transactional
    public String addMember(MemberVO memberVO, SocialUserInfo socialUserInfo) throws Exception {
        if (memberVO == null)
            return "invalid";
            
        memberVO.setPassword(passwordEncoder.encode(memberVO.getPassword()));
        memberMapper.insertMember(memberVO);

        if (socialUserInfo != null) {
            MemberSocialVO memberSocial = new MemberSocialVO();
            memberSocial.setProvideUid(socialUserInfo.getProviderUid());
            memberSocial.setProvider(socialUserInfo.getProvider());
            memberMapper.insertMemberSocial(memberSocial);
        }

        return null;
    }

    /*
     * [login 메서드는 더 이상 컨트롤러에서 직접 호출하지 않음]
     * Spring Security가 MemberUserDetailsService.loadUserByUsername()을 통해
     * 회원을 조회하고, PasswordEncoder로 비밀번호를 비교한다.
     * 하지만 다른 곳에서 사용할 수 있으므로 남겨둔다.
     */
    // public MemberVO login(String id, String password) throws Exception {
    //     MemberVO member = memberMapper.selectMemberById(id);
    //     if (member != null &&
    //         member.getPassword() != null &&
    //         passwordEncoder.matches(password, member.getPassword())) {
    //         member.setPassword(null);
    //         return member;
    //     }

    //     return null;
    // }
}
