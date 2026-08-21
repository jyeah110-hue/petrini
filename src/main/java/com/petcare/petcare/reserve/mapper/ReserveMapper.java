package com.petcare.petcare.reserve.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.petcare.petcare.reserve.vo.ReserveVO;

@Mapper
public interface ReserveMapper {
    public List<ReserveVO> selectReserveList(@Param("bizId") Long bizId,
                                             @Param("fromDate") String fromDate,
                                             @Param("toDate") String toDate) throws Exception;   

    public ReserveVO selectReserveByResvId(String resvId) throws Exception;

    //공통
    
    public void updateReserveStatus(@Param("resvId") String resvId, 
                                    @Param("status") String status);
    
    //숙소
    public ReserveVO selectStayReserveByResvId(String resvId) throws Exception;
    public int countStayReserve(Map<String, Object> param) throws Exception;
    
    public void insertStayReserve(ReserveVO vo) throws Exception;

    //스케줄러
    public int updateReserveStatusPendingToCancel() throws Exception;
    public int updateReserveStatusConfirmToDone() throws Exception;
}
