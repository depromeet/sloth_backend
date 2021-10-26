package com.sloth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sloth.config.auth.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BaseApiController {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TokenProvider tokenProvider;

    protected String accessToken;

    protected final String testEmail = "email@email.com";

    @BeforeEach
    void init() {
        Date accessTokenExpireTime = tokenProvider.createAccessTokenExpireTime();
        accessToken = tokenProvider.createAccessToken(testEmail, accessTokenExpireTime);
    }

}
