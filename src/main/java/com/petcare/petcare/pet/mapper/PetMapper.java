package com.petcare.petcare.pet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.petcare.petcare.pet.vo.PetVO;

@Mapper
public interface PetMapper {
    public List<PetVO> selectPetListByMemberId(String memberId);
    
    public int insertPet(PetVO vo) throws Exception;    
}
