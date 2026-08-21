package com.petcare.petcare.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.petcare.petcare.common.api.KakaoMapService;
import com.petcare.petcare.file.service.FileService;
import com.petcare.petcare.file.vo.FileVO;
import com.petcare.petcare.hospital.service.HospitalService;
import com.petcare.petcare.hospital.vo.HospitalVO;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Controller 
@RequiredArgsConstructor
@RequestMapping("/hospital")
public class HospitalController { 

    private final KakaoMapService kakaoMapService;
    private final HospitalService hospitalService;
    private final FileService fileService;

    @GetMapping({"", "/"})
    public String hospital(@ModelAttribute("search") HospitalVO vo, Model model) throws Exception {
        List<HospitalVO> hospitalList = hospitalService.getHospitalListBySearch(vo);
        kakaoMapService.addMapAttributes(model, hospitalList);
        
        model.addAttribute("hospitalList", hospitalList);
        model.addAttribute("skipAutoMarkers", "true");
        return "hospital/list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam Long hospitalId, Model model) throws Exception {
        HospitalVO hospital = hospitalService.getHospital(hospitalId);
        List<FileVO> imgList = fileService.getFileList("HOSPITAL", hospitalId.toString());

        if (hospital != null && hospital.getLat() != null) {
            List<HospitalVO> singleList = new ArrayList<>();
            singleList.add(hospital); 
            kakaoMapService.addMapAttributes(model, singleList);
        }

        model.addAttribute("hospital", hospital);
        model.addAttribute("imgList", imgList);
        return "hospital/detail";
    }

    @GetMapping("/reserve")
    public String reserve(@RequestParam(defaultValue = "1") String id, Model model) throws Exception {
        model.addAttribute("id", id);
        return "hospital/reserve";
    }

    @GetMapping("/complete")
    public String complete() {
        return "hospital/complete";
    }
}
