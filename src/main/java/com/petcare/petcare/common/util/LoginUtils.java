package com.petcare.petcare.common.util;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class LoginUtils {
    /** 최대 허용 실패 횟수 — 5회 초과 시 잠금 */
    private static final int MAX_ATTEMPTS = 5;

    /** 잠금 시간 (분) — 30분 후 자동 해제 */
    private static final int LOCK_MINUTES = 30;

    /**
     * 로그인 ID 별 실패 정보 저장
     * - ConcurrentHashMap: 여러 사용자가 동시에 로그인 시도해도 안전 (Thread-safe)
     * - key: 로그인 ID (예: "user01")
     * - value: AttemptInfo (실패 횟수 + 잠금 시작 시간)
     */
    private static final ConcurrentHashMap<String, AttemptInfo> attempts = new ConcurrentHashMap<>();

    /**
     * 해당 ID 가 현재 잠금 상태인지 확인
     *
     * @param loginId 로그인 시도 ID
     * @return true = 잠금 상태 (로그인 차단해야 함)
     */
    public static boolean isLocked(String loginId) {
        AttemptInfo info = attempts.get(loginId);
        if (info == null || info.lockedAt == null) {
            return false;
        }

        // 잠금 시간이 지났으면 자동 해제
        if (info.lockedAt.plusMinutes(LOCK_MINUTES).isBefore(LocalDateTime.now())) {
            attempts.remove(loginId);  // 기록 삭제 → 다시 5회 시도 가능
            return false;
        }

        return true;  // 아직 잠금 중
    }

    /**
     * 로그인 실패 기록
     * - 실패 횟수 +1
     * - MAX_ATTEMPTS 초과 시 잠금 처리
     *
     * @param loginId 로그인 실패한 ID
     */
    public static void recordFailure(String loginId) {
        AttemptInfo info = attempts.computeIfAbsent(loginId, k -> new AttemptInfo());
        info.failCount++;

        if (info.failCount >= MAX_ATTEMPTS) {
            info.lockedAt = LocalDateTime.now();  // 잠금 시작
        }
    }

    /**
     * 로그인 성공 시 실패 기록 초기화
     * - 성공했으니까 실패 카운트를 리셋
     *
     * @param loginId 로그인 성공한 ID
     */
    public static void resetAttempts(String loginId) {
        attempts.remove(loginId);
    }

    /**
     * 남은 시도 횟수 반환 (화면에 "N회 남았습니다" 표시용)
     *
     * @param loginId 로그인 ID
     * @return 남은 시도 횟수 (잠금 상태면 0)
     */
    public static int getRemainingAttempts(String loginId) {
        AttemptInfo info = attempts.get(loginId);
        if (info == null) {
            return MAX_ATTEMPTS;
        }
        if (info.lockedAt != null) {
            return 0;
        }
        return Math.max(0, MAX_ATTEMPTS - info.failCount);
    }

    /**
     * 잠금 해제까지 남은 시간(분) 반환 (화면에 "N분 후 시도 가능" 표시용)
     *
     * @param loginId 로그인 ID
     * @return 남은 분 (잠금 아니면 0)
     */
    public static long getRemainingLockMinutes(String loginId) {
        AttemptInfo info = attempts.get(loginId);
        if (info == null || info.lockedAt == null) {
            return 0;
        }
        LocalDateTime unlockAt = info.lockedAt.plusMinutes(LOCK_MINUTES);
        long remaining = java.time.Duration.between(LocalDateTime.now(), unlockAt).toMinutes();
        return Math.max(0, remaining);
    }



    
    /**
     * 실패 정보를 담는 내부 클래스
     */
    private static class AttemptInfo {
        int failCount;                // 연속 실패 횟수
        LocalDateTime lockedAt;       // 잠금 시작 시간 (잠금 안 됐으면 null)

        AttemptInfo() {
            this.failCount = 0;
            this.lockedAt = null;
        }
    }
}
