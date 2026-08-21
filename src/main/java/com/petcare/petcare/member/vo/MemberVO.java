package com.petcare.petcare.member.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
/*
 * [Bean Validation 이란]
 * VO(데이터 객체) 필드에 @NotBlank, @Size 같은 어노테이션을 붙이면,
 * 컨트롤러에서 @Valid 를 선언하는 것만으로 자동 검증됨
 * → 수동 if 검증 코드가 사라지고, VO 자체가 "이 필드는 이런 규칙" 이라는 문서 역할
 * 
 * ══════════════════════════════════════════════════════════
 * [자주 쓰는 어노테이션 정리]
 * ══════════════════════════════════════════════════════════
 *
 * @NotNull      — null 만 차단 (빈 문자열 "" 은 통과)
 * @NotEmpty     — null + 빈 문자열 "" 차단 (공백 " " 은 통과)
 * @NotBlank     — null + "" + " " 전부 차단 (String 전용, 가장 엄격)
 * @Size(min, max) — 문자열 길이 또는 컬렉션 크기 제한
 * @Min(value)   — 숫자 최솟값
 * @Max(value)   — 숫자 최댓값
 * @Pattern(regexp) — 정규식 매칭 (이메일, 전화번호 등)
 * @Email        — 이메일 형식 검증
 * @Positive     — 양수만 허용
 * @PositiveOrZero — 0 이상
 *
 *
 * [Bean Validation의 장점]
 * → VO 자체가 검증 규칙 문서 역할을 합니다.
 *   컨트롤러에 if 문이 흩어져 있으면 어떤 필드에 어떤 규칙인지 파악이 어렵지만,
 *   VO 어노테이션을 보면 한눈에 알 수 있습니다.
 *
 * [BindingResult 는 왜 @Valid 바로 뒤에 와야 하나요?]
 * → Spring 이 파라미터 순서로 "이 BindingResult 는 어떤 @Valid 의 결과인지" 를 매칭합니다.
 *   @Valid 와 BindingResult 사이에 다른 파라미터가 끼면 매칭이 안 돼서
 *   검증 실패 시 400 에러가 바로 발생합니다.
 *
 * [@RequestParam 에는 왜 @Valid 가 안 되나요?]
 * → @Valid 는 객체(VO) 단위 검증입니다. @RequestParam 은 개별 파라미터라서
 *   Bean Validation 대상이 아닙니다. 개별 파라미터에 검증을 걸려면
 *   컨트롤러 클래스에 @Validated 를 붙이고 @RequestParam 앞에 @NotBlank 등을
 *   직접 붙이는 방법이 있지만, VO 로 묶는 게 더 깔끔합니다.
 */

@Getter @Setter
public class MemberVO {
    @NotBlank
    private String id; 
    @NotBlank
    private String password; 
    private String name; 
    @NotBlank
    private String email; 
    private String phone;  
    private String zipcode; 
    private String addr1; 
    private String addr2; 
    private String role; 
    @NotBlank
    private String agreeService; 
    @NotBlank
    private String agreePrivacy;  
    private String agreeLocation;  
    private String agreeMarketing; 
    private Integer point;
    private Date created;
}
