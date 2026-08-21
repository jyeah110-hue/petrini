package com.petcare.petcare.pet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petcare.petcare.pet.mapper.PetMapper;
import com.petcare.petcare.pet.vo.PetVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {
        @Autowired
        private PetMapper petMapper;

    public List<PetVO> getPetList(String memberId) {
        return petMapper.selectPetListByMemberId(memberId);
    }    
}
