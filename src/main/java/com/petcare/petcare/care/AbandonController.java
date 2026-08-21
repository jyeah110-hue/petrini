package com.petcare.petcare.care;

import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.petcare.care.vo.AbandonVO;
import com.petcare.petcare.common.util.HttpUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/care/abandon")
public class AbandonController {

    @Value("${public.service-api-key}")
    private String publicServiceApiKey;

    String baseUrl = "https://apis.data.go.kr/1543061/abandonmentPublicService_v2/abandonmentPublic_v2";

    @GetMapping("/list")
    public String animalList( @RequestParam(defaultValue = "")  String sido,
                              @RequestParam(defaultValue = "")  String sigungu,
                              @RequestParam(defaultValue = "")  String upkind,
                              @RequestParam(defaultValue = "")  String state,
                              @RequestParam(defaultValue = "1") int    pageNo,
                              Model model) {

        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
            String bgnde = LocalDate.now().minusDays(30).format(fmt);
            String endde = LocalDate.now().format(fmt);

            StringBuilder sb = new StringBuilder(baseUrl);
            sb.append("?serviceKey=").append(URLEncoder.encode(publicServiceApiKey, "UTF-8"));
            sb.append("&bgnde=").append(bgnde);
            sb.append("&endde=").append(endde);
            sb.append("&pageNo=").append(pageNo);
            sb.append("&numOfRows=").append(20);
            sb.append("&_type=json");
            if (!sido.isEmpty())    
                sb.append("&sidoLikeCd=").append(URLEncoder.encode(sido, "UTF-8"));
            if (!sigungu.isEmpty()) 
                sb.append("&sigunguLikeCd=").append(URLEncoder.encode(sigungu,"UTF-8"));
            if (!upkind.isEmpty())  
                sb.append("&upkind=").append(upkind);
            if (!state.isEmpty())   
                sb.append("&state=").append(state);

            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            String response = HttpUtils.request(sb.toString(), "GET", headers, null);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode body = root.path("response").path("body");
            int totalCount = body.path("totalCount").asInt(0);
            JsonNode items = body.path("items").path("item");

            List<AbandonVO> animals = new ArrayList<>();
            if (items.isArray()) {
                for (JsonNode item : items) animals.add(AbandonVO.parseItem(item, fmt));
            } else if (items.isObject() && !items.isEmpty()) {
                animals.add(AbandonVO.parseItem(items, fmt));
            }

            model.addAttribute("animals",    animals);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("pageNo",     pageNo);
            model.addAttribute("totalPages", (int) Math.ceil((double) totalCount / 20));
            model.addAttribute("sido",       sido);
            model.addAttribute("upkind",     upkind);
            model.addAttribute("state",      state);
            model.addAttribute("apiError",   false);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("animals",    new ArrayList<>());
            model.addAttribute("totalCount", 0);
            model.addAttribute("pageNo",     1);
            model.addAttribute("totalPages", 0);
            model.addAttribute("apiError",   true);
            model.addAttribute("errorMsg",   e.getMessage());
        }
        return "give/animal/list";
    }
    
    // ── 유기동물 상세 ─────────────────────────────────────
    @GetMapping("/detail")
    public String animalDetail(@RequestParam String desertionNo, Model model) {
        try {
            StringBuilder sb = new StringBuilder(baseUrl);
            sb.append("?serviceKey=").append(URLEncoder.encode(publicServiceApiKey, "UTF-8"));
            sb.append("&desertion_no=").append(URLEncoder.encode(desertionNo, "UTF-8"));
            sb.append("&_type=json");

            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            String response = HttpUtils.request(sb.toString(), "GET", headers, null);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            // 결과가 1건이면 객체로, 여러 건이면 배열로 내려오는 경우가 있어 둘 다 처리
            JsonNode item = items.isArray() ? items.get(0) : items;

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
            model.addAttribute("animal",   AbandonVO.parseItem(item, fmt));
            model.addAttribute("apiError", false);
        } 
        catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("animal",   null);
            model.addAttribute("apiError", true);
        }
        return "give/animal/detail";
    }
}
