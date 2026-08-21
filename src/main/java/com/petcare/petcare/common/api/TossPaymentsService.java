package com.petcare.petcare.common.api;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.petcare.common.exception.ResponseExceptionHandler;
import com.petcare.petcare.common.util.HttpUtils;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TossPaymentsService {
    @Value("${toss.secret-key}")
    private String tossSecretKey;

    public String confirmPayment(String paymentKey, String orderId, int amount) throws Exception {
        try {
            String url = "https://api.tosspayments.com/v1/payments/confirm";
            String encodedAuth = Base64.getEncoder().encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));

            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Basic " + encodedAuth);
            headers.put("Content-Type", "application/json");

            String body = "{\"paymentKey\":\"" + paymentKey + "\",\"orderId\":\"" + orderId + "\",\"amount\":" + amount + "}";

            String response = HttpUtils.request(url, "POST", headers, body);
            return null;
        } 
        catch (ResponseExceptionHandler  e) {
            // HttpUtil이 실패 시 "API 오류: HTTP 400, body={...}" 형태로 던짐
            String msg = e.getMessage();
            try {
                // body= 뒤의 JSON에서 토스 에러 메시지 추출
                JsonNode json = new ObjectMapper().readTree(e.getResponseBody());
                return json.path("message").asText("토스 결제승인 요청이 거절되었습니다.");
            } catch (Exception ex) {
                return "토스 결제승인 요청이 거절되었습니다.";
            }            
        }
        catch (Exception e) {
            return "토스 승인 API 호출 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    public String cancelPayment(String paymentKey, String cancelReason) throws Exception {
        try {
            String url = "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel";
            String encodedAuth = Base64.getEncoder().encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));

            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Basic " + encodedAuth);
            headers.put("Content-Type", "application/json");

            String body = "{\"cancelReason\":\"" + cancelReason.replace("\"", "'") + "\"}";
            String response = HttpUtils.request(url, "POST", headers, body);
            return null;
        } 
        catch (ResponseExceptionHandler e) {
            String msg = e.getMessage();
            try {
                // body= 뒤의 JSON에서 토스 에러 메시지 추출
                JsonNode json = new ObjectMapper().readTree(e.getResponseBody());
                return json.path("message").asText("토스 결제승인 요청이 거절되었습니다.");
            } catch (Exception ex) {
                return "토스 결제취소 요청이 거절되었습니다.";
            }   
        }
        catch (Exception e) {
            return "토스 API 호출 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}
