package com.petcare.petcare.store.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryVO {
    private int categoryId;
    private int parentId;
    private String name;
    private int depth;
    private int status;
}
