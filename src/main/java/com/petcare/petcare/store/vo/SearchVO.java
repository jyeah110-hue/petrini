package com.petcare.petcare.store.vo;

import java.util.List;

import com.petcare.petcare.common.vo.SearchBaseVO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SearchVO extends SearchBaseVO {
    private Integer species;
    private Integer age;
    private Integer type;
    private String keyword;
    private Integer maxPrice;
    private List<String> brand;  
}
