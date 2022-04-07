package com.sloth.api.fcm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sloth.api.fcm.dto.FcmDto;
import com.sloth.api.fcm.service.FirebaseCloudMessageService;
import com.sloth.test.base.NewBaseApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class FcmControllerTest extends NewBaseApiController {

    @InjectMocks
    FcmController fcmController;

    @Mock
    FirebaseCloudMessageService firebaseCloudMessageService;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(fcmController)
                .build();
    }

    @Test
    @DisplayName("fcm 푸시 메세지 테스트")
    void pushMessageTest() throws Exception {

        FirebaseCloudMessageService firebaseCloudMessageService = mock(FirebaseCloudMessageService.class);

        // given
        FcmDto.Request requestDTO = FcmDto.Request.builder()
                .targetToken("targetToken")
                .title("푸시 알림")
                .body("푸시 메세지")
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/api/fcm")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                ;

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

}