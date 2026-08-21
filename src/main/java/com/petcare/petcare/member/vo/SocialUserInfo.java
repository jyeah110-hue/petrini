package com.petcare.petcare.member.vo;

public interface SocialUserInfo {
    String getProviderUid();   // 소셜 고유 ID
    String getProvider();      // "KAKAO", "NAVER", "GOOGLE"
    String getEmail();
    String getAccessToken();    
}
