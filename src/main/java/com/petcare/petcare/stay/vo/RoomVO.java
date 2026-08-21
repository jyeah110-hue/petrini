package com.petcare.petcare.stay.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomVO {
    private Long roomId;
    private Long stayId;
    private String name;
    private int price;
    private int maxPeople;
    private int maxPet;
    private Date created;
}
