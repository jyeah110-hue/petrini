package com.petcare.petcare.stay.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petcare.petcare.common.api.KakaoMapService;
import com.petcare.petcare.reserve.mapper.ReserveMapper;
import com.petcare.petcare.stay.mapper.StayMapper;
import com.petcare.petcare.stay.vo.RoomVO;
import com.petcare.petcare.stay.vo.StayVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StayService {
    private final KakaoMapService kakaoMapService;
    private final StayMapper stayMapper;
    private final ReserveMapper reserveMapper;

    public List<StayVO> getStayList() throws Exception {
        return stayMapper.selectStayList();
    }

    public List<StayVO> getStayListBySearch(StayVO vo) throws Exception {
        return stayMapper.selectStayListBySearch(vo);
    }

    public StayVO getStay(Long stayId) throws Exception {
        StayVO stay = stayMapper.selectStay(stayId);
        if (stay != null) {
            List<RoomVO> rooms = stayMapper.selectRoomListByStayId(stayId);
            stay.setRooms(rooms);            
        }

        return stay;
    }

    public StayVO getStayByMemberId(String memberId) throws Exception {
        StayVO stay =  stayMapper.selectStayByMemberId(memberId);
        if (stay != null) {
            List<RoomVO> rooms = stayMapper.selectRoomListByStayId(stay.getStayId());
            stay.setRooms(rooms);            
        }

        return stay;
    }  
    
    public StayVO getStayByBizId(Long bizId) throws Exception {
        StayVO stay =  stayMapper.selectStayByBizId(bizId);
        if (stay != null) {
            List<RoomVO> rooms = stayMapper.selectRoomListByStayId(stay.getStayId());
            stay.setRooms(rooms);            
        }

        return stay;
    }

    @Transactional
    public void updateStay(StayVO vo) throws Exception {
        if (vo.getAddr1() != null && !vo.getAddr1().isBlank()) {
            Map<String, Double> coords = kakaoMapService.geocodeAddress(vo.getAddr1());
            if (coords != null) {
                vo.setLat(coords.get("lat"));
                vo.setLng(coords.get("lng"));
            }
        }

        stayMapper.updateStay(vo);
    }


    //객실
    public List<RoomVO> getRoomListByStayId(Long stayId) throws Exception {
        return stayMapper.selectRoomListByStayId(stayId);
    }

    public void selectRoom(Long roomId) throws Exception {
        stayMapper.selectRoom(roomId);
    }

    public void insertRoom(RoomVO vo) throws Exception {
        stayMapper.insertRoom(vo);
    }

    public void updateRoom(RoomVO vo) throws Exception {
        stayMapper.updateRoom(vo);
    }

    public void deleteRoom(Long roomId, Long stayId) throws Exception {
        stayMapper.deleteRoom(roomId, stayId);
    }

    //예약가능체크: 단순 조회(락 X)
    public boolean checkRoomAvailability(Long roomId, Date checkinDate, Date checkoutDate) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("roomId", roomId);
        param.put("checkinDate", checkinDate);
        param.put("checkoutDate", checkoutDate);
        int count = reserveMapper.countStayReserve(param);
        return count == 0;
    }

}
