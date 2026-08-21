package com.petcare.petcare.biz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.petcare.petcare.biz.vo.BusinessVO;

@Mapper
public interface BizMapper {
    public List<BusinessVO> selectBusinessList(String status) throws Exception;

    public BusinessVO selectBusinessByMemberId(String memberId) throws Exception;
    public BusinessVO selectBusinessByBizId(String bizId) throws Exception;

    public void insertBusiness(BusinessVO vo) throws Exception;

    //매개변수가 2개 이상일때 @Param이 없으면 MyBatis가 구분못함.
    public int updateBusinessStatus(@Param("bizId") Long bizId,
                                    @Param("status") String status) throws Exception;    

    public int countBusinessByStatus(String status);
}
