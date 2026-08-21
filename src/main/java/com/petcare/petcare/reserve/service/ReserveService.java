package com.petcare.petcare.reserve.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.petcare.petcare.reserve.mapper.ReserveMapper;
import com.petcare.petcare.reserve.vo.ReserveVO;
import com.petcare.petcare.stay.mapper.StayMapper;
import com.petcare.petcare.stay.vo.RoomVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReserveService {
    private final ReserveMapper reserveMapper;
    private final StayMapper stayMapper;

    public List<ReserveVO> getReserveList(Long bizId, String fromDate, String toDate) throws Exception {
        if (bizId == null) {
            return List.of();
        }
        return reserveMapper.selectReserveList(bizId, fromDate, toDate);        
    }

    public ReserveVO getStayReserveByResvId(String resvId) throws Exception {
        return reserveMapper.selectStayReserveByResvId(resvId);
    }

    public String createStayReserve(ReserveVO vo) throws Exception {
        // 1. 객실 행 잠금
        RoomVO room = stayMapper.selectRoomForUpdate(vo.getRoomId());
        if (room == null) {
            throw new RuntimeException("존재하지 않는 객실입니다.");
        }

        // 2. 날짜 겹침 확인
        Map<String, Object> param = new HashMap<>();
        param.put("roomId", vo.getRoomId());
        param.put("checkinDate", vo.getCheckinDate());
        param.put("checkoutDate", vo.getCheckoutDate());
        int overlap = reserveMapper.countStayReserve(param);
        if (overlap > 0) {
            throw new RuntimeException("해당 날짜에 이미 예약이 있습니다.");
        }

        // 3. 금액 계산 (서버에서 재계산)
        int nights = vo.getNights();
        int totalAmount = room.getPrice() * nights;
        vo.setTotalAmount(totalAmount);

        // 4. PENDING 상태로 예약 INSERT
        vo.setBizType("STAY");
        vo.setStatus("PENDING");
        vo.setResvId("S" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        reserveMapper.insertStayReserve(vo);
        
        return vo.getResvId();
    }
}
