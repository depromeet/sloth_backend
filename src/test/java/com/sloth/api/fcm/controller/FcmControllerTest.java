package com.sloth.api.fcm.controller;

import com.sloth.api.fcm.dto.FcmDto;
import com.sloth.api.fcm.service.FirebaseCloudMessageService;
import com.sloth.test.base.BaseApiController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = FcmController.class)
class FcmControllerTest extends BaseApiController {

    @MockBean
    private FirebaseCloudMessageService firebaseCloudMessageService;

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

        doNothing().when(firebaseCloudMessageService).sendMessageTo(requestDTO.getTargetToken(), requestDTO.getTitle(), requestDTO.getBody());

        // when
        ResultActions result = mockMvc.perform(post("/api/fcm")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                ;

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk())
        ;
    }

}