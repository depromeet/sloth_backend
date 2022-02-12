package com.sloth.api.logout.controller;

import com.sloth.api.logout.controller.LogoutController;
import com.sloth.api.logout.service.LogoutService;
import com.sloth.global.config.auth.TokenProvider;
import com.sloth.test.base.NewBaseApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LogoutControllerTest extends NewBaseApiController {

    @InjectMocks
    LogoutController logoutController;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    LogoutService logoutService;

    MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(logoutController).build();
    }

    //@Test
    @DisplayName("로그아웃 테스트 - localDateTime.now() 때문에 테스트 불가능 ㅠ")
    void logoutTest() throws Exception {

        // given
        String email = "test@test.com";
        doNothing().when(logoutService).logout(email, LocalDateTime.now());

        BDDMockito.when(tokenProvider.getEmail("accessToken")).thenReturn(email);

        // when
        ResultActions result = mockMvc.perform(post("/api/logout")
                .header(HttpHeaders.AUTHORIZATION, "accessToken")
                .accept(MediaType.TEXT_PLAIN_VALUE)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(content().string("logout success"));
    }

}