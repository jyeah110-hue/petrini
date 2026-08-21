package com.petcare.petcare.stay.vo;

import java.util.List;

import com.petcare.petcare.common.api.MapMarkerable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StayVO implements MapMarkerable {
    private Long   stayId;
    private Long   bizId;
    private String name;
    private String phone;
    private String zipcode;
    private String addr1;
    private String addr2;
    private Double lat;
    private Double lng;
    private String description;
    private String tags;    
    private String checkIn;       
    private String checkOut;     
    private int petFee;
    private int totalAmount;
    private String petPolicy;
    private String refundPolicy;
    private String region;
    private String thumb;

    private List<RoomVO> rooms;

    // ── 목록용 (SQL에서 계산) ──
    private int minPrice;
    private int cntRoom;

    // ── 검색 조건 (list 화면용) ──
    private int maxPrice; 
    private String sort;
    private String[] tagFilter;

    // ── 지도 마커용 ──
    public String getMarkerId() { return stayId != null ? String.valueOf(stayId) : "0"; }
    public String getMarkerName() { return name; }
    public Double getMarkerLat() { return lat; }
    public Double getMarkerLng() { return lng; }    
}
