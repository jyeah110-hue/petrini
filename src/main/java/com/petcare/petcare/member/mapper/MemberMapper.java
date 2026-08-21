package com.petcare.petcare.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.petcare.petcare.member.vo.MemberSocialVO;
import com.petcare.petcare.member.vo.MemberVO;

@Mapper
public interface MemberMapper {
    //회원가입
    public int countMemberById(@Param("id") String id) throws Exception;
    public int countMemberByEmail(@Param("email") String email) throws Exception;
    public int insertMember(MemberVO vo) throws Exception;    
    public int insertMemberSocial(MemberSocialVO vo);
    
    //로그인
    public MemberVO selectMemberById(@Param("id") String id) throws Exception;

    //승인
    public int updateMemberRole(@Param("id") String id,
                                @Param("role") String role) throws Exception;  
}
