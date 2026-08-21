package com.petcare.petcare.biz.vo;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BusinessVO {
    @NotBlank
    private Long bizId; 
    @NotBlank
    private String memberId;
    @NotBlank
    private String type;
    @NotBlank
    private String bizNo;
    @NotBlank
    private String name;
    private String ceo;
    private String phone;
    @NotBlank
    private String zipcode;
    @NotBlank
    private String addr1;
    private String addr2;
    @NotBlank
    private String status;
    private Date approved;
    private Date created;
}
