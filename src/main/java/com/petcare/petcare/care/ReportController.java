package com.petcare.petcare.care;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/care/report")
public class ReportController {
    @GetMapping("/list")
    public String reportList() {
        return "care/report/list";
    }
}
