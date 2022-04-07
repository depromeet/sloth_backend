package com.sloth.api.login.oauth.controller;

import com.sloth.api.login.oauth.service.OauthLoginService;
import com.sloth.global.config.auth.TokenProvider;
import com.sloth.test.base.BaseApiController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OauthLoginControllerTest extends BaseApiController {

    @InjectMocks
    private OauthLoginController oauthLoginController;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private OauthLoginService oauthLoginService;

    private MockMvc mockMvc;

    private String testPassword = "testPassword";



}