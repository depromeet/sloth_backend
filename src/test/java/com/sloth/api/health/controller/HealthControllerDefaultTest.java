package com.sloth.api.health.controller;

import com.sloth.global.resolver.CurrentEmailArgumentResolver;
import com.sloth.test.base.BaseApiController;
import com.sloth.test.base.NewBaseApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HealthControllerDefaultTest extends NewBaseApiController {

    private MockMvc mockMvc;

    @InjectMocks
    private HealthController healthController;

    @Mock
    Environment env;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(healthController)
                .build();
    }

    @Test
    @DisplayName("profile default api 테스트")
    public void profile()  throws Exception {

        // given
        String[] profiles = {};
        BDDMockito.when(env.getActiveProfiles()).thenReturn(profiles);

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