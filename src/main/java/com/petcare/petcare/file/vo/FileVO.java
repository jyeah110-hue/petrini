package com.petcare.petcare.file.vo;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter @Setter
public class FileVO {
    private Long fileId;
    private String refId;
    private String refType;
    private String url;
    private String filename;   
    private Date created;    
}