package com.petcare.petcare.reserve.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.petcare.petcare.reserve.mapper.ReserveMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReserveScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReserveScheduler.class);

    private final ReserveMapper reserveMapper;

    @Scheduled(fixedRate = 300000)
    public void cancleStayReserve() {
        try {
            int cntCanceled = reserveMapper.updateReserveStatusPendingToCancel();
            if (cntCanceled > 0) {
                LOGGER.info("[ReserveScheduler] 만료 PENDING 예약 자동 취소 — {}건 PENDING → CANCEL", cntCanceled);
            }
        } 
        catch (Exception e) {
            LOGGER.error("[ReserveScheduler] 만료 PENDING 자동 취소 실패", e);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void doneStayReserve() {
        try {
            int cntDone = reserveMapper.updateReserveStatusConfirmToDone();
            if (cntDone > 0) {
                LOGGER.info("[ReserveScheduler] 숙박 완료 자동 처리 — {}건 CONFIRMED → DONE", cntDone);
            }
        } catch (Exception e) {
            LOGGER.error("[ReserveScheduler] 숙박 완료 자동 처리 실패", e);
        }
    }
}
