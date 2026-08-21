package com.petcare.petcare.common.api;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.petcare.petcare.common.util.HttpUtils;
import com.petcare.petcare.reserve.vo.ReserveVO;

@Service
public class KakaoMemoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KakaoMemoService.class);
    private static final String MEMO_SEND_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    private boolean send(String accessToken, String templateObjectJson) {
        try {
            // 요청 본문 (form-urlencoded)
            String body = "template_object=" + URLEncoder.encode(templateObjectJson, "UTF-8");

            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + accessToken);
            headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            String response = HttpUtils.request(MEMO_SEND_URL, "POST", headers, body);
            if (response != null) {
                LOGGER.info("[KakaoMemo] 나에게 보내기 성공");
                return true;                
            }
            else {
                LOGGER.warn("[KakaoMemo] 나에게 보내기 실패");
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("[KakaoMemo] 나에게 보내기 예외", e);
            return false;
        }
    }

    public boolean sendStayReserveMessage(String accessToken, ReserveVO vo) {

        if (accessToken == null || accessToken.isBlank()) {
            LOGGER.warn("[KakaoMsg] accessToken 없음 — 카카오 로그인 사용자가 아닙니다");
            return false;
        }

        // 메시지 텍스트 구성
        StringBuilder text = new StringBuilder();
        text.append("숙소 예약이 확정되었습니다!\n\n");
        text.append("예약번호: ").append(vo.getResvId()).append("\n");
        text.append("숙소: ").append(vo.getBizName()).append("\n");
        text.append("객실: ").append(vo.getRoomName()).append("\n");
        text.append("").append(vo.getCheckinDate()).append(" ~ ").append(vo.getCheckoutDate());
        text.append("결제금액: ").append(String.format("%,d", vo.getTotalAmount())).append("원");

        // template_object JSON 구성 (text 타입)
        String templateObject = "{"
            + "\"object_type\":\"text\","
            + "\"text\":\"" + HttpUtils.escapeJson(text.toString()) + "\","
            + "\"link\":{"
            +   "\"web_url\":\"http://localhost:8080/mypage/reserve\","
            +   "\"mobile_web_url\":\"http://localhost:8080/mypage/reserve\""
            + "},"
            + "\"button_title\":\"예약 내역 보기\""
            + "}";

        return send(accessToken, templateObject);
    }
}
