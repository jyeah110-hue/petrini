package com.petcare.petcare.stay.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.petcare.petcare.stay.vo.RoomVO;
import com.petcare.petcare.stay.vo.StayVO;

@Mapper
public interface StayMapper {

    //숙소
    public List<StayVO> selectStayList() throws Exception;
    public List<StayVO> selectStayListBySearch(StayVO vo) throws Exception;

    public StayVO selectStay(Long stayId) throws Exception;
    public StayVO selectStayByMemberId(String memberId) throws Exception;
    public StayVO selectStayByBizId(Long bizId) throws Exception;

    public int insertStay(Long bizId) throws Exception;
    public int updateStay(StayVO vo) throws Exception;

    //객실
    public List<RoomVO> selectRoomListByStayId(Long stayId) throws Exception; 

    public RoomVO selectRoom(Long roomId) throws Exception;
    public RoomVO selectRoomForUpdate(Long roomId) throws Exception;

    public int insertRoom(RoomVO vo) throws Exception;
    public int updateRoom(RoomVO vo) throws Exception;
    public int deleteRoom(@Param("roomId") Long roomId, 
                          @Param("stayId") Long stayId) throws Exception;


    //예약

}
