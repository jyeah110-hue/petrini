package com.petcare.petcare.payment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petcare.petcare.reserve.mapper.ReserveMapper;
import com.petcare.petcare.reserve.vo.ReserveVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final ReserveMapper reserveMapper;
    private final PaymentMapper paymentMapper;
    
    @Transactional
    public void confirmPayment(ReserveVO reserve, String tossPaymentKey, String tossOrderId, String payMethod) throws Exception {
        // 결제 정보 INSERT
        PaymentVO vo = new PaymentVO();
        vo.setResvId(reserve.getResvId());
        vo.setMethod(payMethod);
        vo.setAmount(reserve.getTotalAmount());
        vo.setPaymentKey(tossPaymentKey);
        vo.setOrderId(tossOrderId);
        vo.setStatus("DONE");
        paymentMapper.insertPayment(vo);

        // 예약 상태 → CONFIRMED
        reserveMapper.updateReserveStatus(reserve.getResvId(), "CONFIRMED");
    }    
}
