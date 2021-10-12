package com.sloth.api.test.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api2")
public class AuthTestController {

    @GetMapping(value = "test", produces = MediaType.TEXT_PLAIN_VALUE)
    public String test() {
        return "test";
    }

}
