package com.sloth.api.health.controller;

import com.sloth.api.BaseApiController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HealthControllerTest extends BaseApiController {

    @Test
    @DisplayName("profile api 테스트")
    public void profile()  throws Exception {

        //when
        ResultActions result = mockMvc.perform(
                get("/api/profile")
                        .accept(MediaType.TEXT_PLAIN_VALUE)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(content().string("default"));
    }

    @Test
    @DisplayName("health check api 조회 테스트")
    public void healthCheck() throws Exception {

        //when
        ResultActions result = mockMvc.perform(
                get("/api/health")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status", is(true)))
                .andExpect(jsonPath("$.health", is("ok")))
                ;

    }
}