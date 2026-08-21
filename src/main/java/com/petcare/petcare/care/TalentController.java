package com.petcare.petcare.care;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/care/talent")
public class TalentController {
    @GetMapping("/list")   
    public String talentList() {
        return "care/talent/list";
    }
}
