package com.petcare.petcare.care;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/care")
public class CareController {

    @GetMapping({"", "/"})
    public String give() {
        return "redirect:/care/abandon/list";
    }
}
