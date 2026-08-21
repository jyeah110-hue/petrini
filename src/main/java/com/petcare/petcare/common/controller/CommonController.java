package com.petcare.petcare.common.controller;

import com.petcare.petcare.common.api.KakaoMapService;

//상속용 베이스 클래스지 직접 요청을 받는 컨트롤러가 아님
//어노테이션 삭제해도 자식 클래스들이 갖고있으니 아무 문제 없음
// @Controller("commonController")
public class CommonController { 
    //@Autowired 필드 주입성
    //public ApiService apiService;

    /*
        final을 사용하지 않아도 기능적 문제는 없지만 권장되는 형태
        final필드는 반드시 생성 시점에 한 번 초기화 되어야 한다.
        선언과 함께 new로 초기화 ->  Spring에서 관리하는 Bean을 직접 new로 생성하기 때문에
                                    Spring에서는 이렇게 하면 안된다.
    */
    public final KakaoMapService kakaoMapService;

    //@RequiredArgsConstructor
    //컴파일 시 아래와 같은 생성자를 자동으로 만들어준다
    public CommonController(KakaoMapService kakaoMapService) {
        this.kakaoMapService = kakaoMapService;
    }
}
