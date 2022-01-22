package com.sloth.api.health.controller;

import com.sloth.api.BaseApiController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("real1")
@Execution(ExecutionMode.CONCURRENT)
@WebMvcTest(controllers = HealthController.class)
public class HealthControllerProfile1Test extends BaseApiController {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("profile real1 api 테스트")
    public void profile()  throws Exception {

        //when
        ResultActions result = mockMvc.perform(
                get("/api/profile")
                        .accept(MediaType.TEXT_PLAIN_VALUE)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(content().string("real1"));
    }

}