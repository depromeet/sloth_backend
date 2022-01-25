package com.sloth.api.health.controller;

import com.sloth.test.base.BaseApiController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Execution(ExecutionMode.CONCURRENT)
@WebMvcTest(controllers = HealthController.class)
public class HealthControllerDefaultTest extends BaseApiController {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("profile default api 테스트")
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