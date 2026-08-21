package com.petcare.petcare.hospital.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.petcare.petcare.hospital.vo.HospitalVO;

@Mapper
public interface HospitalMapper {
    public List<HospitalVO> selectHospitalList() throws Exception;
    public List<HospitalVO> selectHospitalListBySearch(HospitalVO vo) throws Exception;

    public HospitalVO selectHospital(Long hospitalId) throws Exception;
    public HospitalVO selectHospitalByMemberId(String memberId) throws Exception;
    public HospitalVO selectHospitalByBizId(Long bizId) throws Exception;

    public int insertHospital(Long bizId) throws Exception;

    public int updateHospital(HospitalVO vo) throws Exception;
}
