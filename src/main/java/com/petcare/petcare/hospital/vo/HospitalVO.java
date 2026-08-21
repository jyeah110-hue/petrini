package com.petcare.petcare.hospital.vo;

import java.util.Date;

import com.petcare.petcare.common.api.MapMarkerable;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HospitalVO implements MapMarkerable {
    private Long hospitalId; 
    private Long bizId; 
    private String name; 
    private String phone; 
    private String zipcode; 
    private String addr1; 
    private String addr2; 
    private Double lat; 
    private Double lng; 
    private String description;  
    private String tags;  
    private String timetable;
    private String status; 
    private Date created;
    private String thumb;      

    // ── 검색 조건 (list 화면용) ──
    private String keyword;        // 지역명·병원명 텍스트 검색
    private String tagFilter;     // 진료과목 (24시간 진료, 특수동물 진료, 입원진료 가능, 호스피텔 가능)
    private String target;         // 진료 대상 (강아지, 고양이, 특수동물)
    private String sort;           // 정렬 (rating, review)

    @Override
    public String getMarkerId() { return String.valueOf(hospitalId); }
    @Override
    public String getMarkerName() { return name; }
    @Override
    public Double getMarkerLat() { return lat; }
    @Override
    public Double getMarkerLng() { return lng; }
}
