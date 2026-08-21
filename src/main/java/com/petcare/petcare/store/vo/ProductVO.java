package com.petcare.petcare.store.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductVO {
    private int productId;
    private String code;
    private String name;
    private String brand;
    private int categoryId;
    private int price;
    private int sale;

    // 추가 필드
    private String thumb;        // 썸네일 이미지
    private int discountRate;    // 할인율
}
