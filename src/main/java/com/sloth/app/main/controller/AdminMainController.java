package com.sloth.app.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sloth.domain.member.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminMainController {

    @GetMapping("/")
    public String main(Member member){
        return "main/adminMain";
    }

}
