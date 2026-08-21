package com.petcare.petcare.pet.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;

@Getter @Setter
public class PetVO {
    public Integer petId;
    public String memberId;
    @NotBlank
    public String petName;
    @NotBlank
    public String petType;
    @NotBlank
    public String species;
    public String gender;
    public Integer age;
    public Integer weight;
    public Date created;
    private String thumb;
}
