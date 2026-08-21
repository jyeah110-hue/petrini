package com.petcare.petcare.common.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.petcare.common.util.HttpUtils;
import com.petcare.petcare.member.vo.KakaoUserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {
    @Value("${kakao.rest-api-key}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;   
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public String getAccessToken(String code) throws Exception {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        String params = "grant_type=authorization_code"
                      + "&client_id=" + clientId
                      + "&client_secret=" + clientSecret
                      + "&redirect_uri=" + redirectUri
                      + "&code=" + code;

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        String response = HttpUtils.request(tokenUrl, "POST", headers, params);
        if (response == null) return null;

        try {
            return objectMapper.readTree(response).path("access_token").asText(null);
        } 
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public KakaoUserVO getUserInfo(String accessToken) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        String response = HttpUtils.request("https://kapi.kakao.com/v2/user/me", "GET", headers, null);
        if (response == null) return null;

        try {
            KakaoUserVO user = new KakaoUserVO();

            JsonNode root = objectMapper.readTree(response);
            JsonNode account = root.path("kakao_account");
            JsonNode profile = account.path("profile");

            user.setKakaoId(String.valueOf(root.path("id").asLong()));
            user.setNickname(profile.path("nickname").asText("카카오회원"));
            user.setProfileImage(profile.path("profile_image_url").asText(null));
            user.setAccessToken(accessToken);
    
            // 이메일 (동의 항목에 포함된 경우에만)
            if (account.path("has_email").asBoolean(false) && 
                account.path("is_email_valid").asBoolean(false)) {
                user.setEmail(account.path("email").asText(null));
            }

            return user;
        } 
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getAuthorizeUrl() {
        return "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=talk_message";
    }
}
