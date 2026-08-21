package com.petcare.petcare.reserve.vo;

import java.util.Date;
import java.util.List;

import lombok.Setter;
import lombok.Getter;

@Getter @Setter
public class ReserveVO {
    //공통
    private String resvId;
    private String bizType;
    private Long bizId;
    private String memberId;
    private String memo;
    private String status;
    private String reject;
    private Date created;
    
    private List<Long> pets;

    //병원
    private String resvDate;
    private String resvTime;
    private String symptoms;
    
    //숙소
    private Long roomId;
    private Date checkinDate;
    private Date checkoutDate;
    private int totalAmount;
    
    //예약 상세정보
    private String bizName;
    private String roomName;
    private int nights;             //숙소
}
