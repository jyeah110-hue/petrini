package com.petcare.petcare.hospital.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petcare.petcare.common.api.KakaoMapService;
import com.petcare.petcare.hospital.mapper.HospitalMapper;
import com.petcare.petcare.hospital.vo.HospitalVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospitalService {
    private final KakaoMapService kakaoMapService;
    private final HospitalMapper hospitalMapper; 

    public List<HospitalVO> getHospitalList() throws Exception {
        return hospitalMapper.selectHospitalList();
    }

    public List<HospitalVO> getHospitalListBySearch(HospitalVO vo) throws Exception {
        return hospitalMapper.selectHospitalListBySearch(vo);
    }

    public HospitalVO getHospital(Long hospitalId) throws Exception {
        return hospitalMapper.selectHospital(hospitalId);
    }  

    public HospitalVO getHospitalByMemberId(String memberId) throws Exception {
        return hospitalMapper.selectHospitalByMemberId(memberId);
    }  
    
    public HospitalVO getHospitalByBizId(Long bizId) throws Exception {
        return hospitalMapper.selectHospitalByBizId(bizId);
    }

    @Transactional
    public void updateHospital(HospitalVO vo) throws Exception {
        if (vo.getAddr1() != null && !vo.getAddr1().isBlank()) {
            Map<String, Double> coords = kakaoMapService.geocodeAddress(vo.getAddr1());
            if (coords != null) {
                vo.setLat(coords.get("lat"));
                vo.setLng(coords.get("lng"));
            }
        }

        hospitalMapper.updateHospital(vo);
    }
}
