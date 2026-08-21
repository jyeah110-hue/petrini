package com.petcare.petcare.file.controller;

import com.petcare.petcare.common.api.KakaoMapService;
import com.petcare.petcare.common.api.KakaoMemoService;
import com.petcare.petcare.common.controller.CommonController;

//controller를 상속해서 공통 Bean을 사용하는 구조는 많이 사용하지 않음
public class FileController extends CommonController{

    private final KakaoMemoService kakaoMessageService;
    
    //@RequiredArgsConstructor 할 경우 kakaoMessageService만 초기화하는 생성자만 생성
    //따라서 생성자 따로 정의해야함 -> 코드 번거로워짐
    public FileController(KakaoMapService kakaoMapService,
                          KakaoMemoService kakaoMessageService) {
        //super(..) 생략할 경우 자동으로 super()를 호출
        super(kakaoMapService);
        this.kakaoMessageService = kakaoMessageService;                        
    }
}
