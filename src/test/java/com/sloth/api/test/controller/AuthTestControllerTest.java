package com.sloth.api.test.controller;

import com.sloth.api.BaseApiController;
import com.sloth.config.auth.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Calendar;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthTestControllerTest extends BaseApiController {

    @Autowired
    private TokenProvider tokenProvider;

    @Test
    @DisplayName("Access Token 없을 경우 401 에러 테스트")
    public void noAccessTokenTest() throws Exception {
        //when
        ResultActions result = mockMvc.perform(get("/api/test")
                .accept(MediaType.TEXT_PLAIN_VALUE));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Access Token 만료된 경우 401 에러 테스트")
    public void accessTokenExpiredTest() throws Exception {

        //given
        String email = "test@gmail.com";
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.OCTOBER, 2);
        Date accessTokenExpireTime = calendar.getTime();
        String accessToken = tokenProvider.createAccessToken(email, accessTokenExpireTime);

        //when
        ResultActions result = mockMvc.perform(get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.TEXT_PLAIN_VALUE));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Access Token 변조된 경우 403 에러 테스트")
    public void accessTokenForbiddenTest() throws Exception {

        //given
        String email = "test@gmail.com";
        String accessToken = "errortoken";

        //when
        ResultActions result = mockMvc.perform(get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.TEXT_PLAIN_VALUE));

        //then
        result.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Access Token 정상적으로 요청하는 경우 테스트")
    public void accessTokenAuthTest() throws Exception {

        //given
        String email = "test@gmail.com";
        Date accessTokenExpireTime = tokenProvider.createAccessTokenExpireTime();
        String accessToken = tokenProvider.createAccessToken(email, accessTokenExpireTime);

        //when
        ResultActions result = mockMvc.perform(get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.TEXT_PLAIN_VALUE));

        //then
        result.andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("test"));
    }

}