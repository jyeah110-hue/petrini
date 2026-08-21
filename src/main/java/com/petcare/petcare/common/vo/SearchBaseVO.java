package com.petcare.petcare.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SearchBaseVO {
    private Integer curPage = 1;
    private Integer pageSize = 12;
    private String sort;

    // MyBatis LIMIT 계산용
    public int getOffset() {
        return (curPage - 1) * pageSize;
    }    
}
