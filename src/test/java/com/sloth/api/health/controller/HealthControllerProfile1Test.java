package com.sloth.api.health.controller;

import com.sloth.test.base.NewBaseApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HealthControllerProfile1Test extends NewBaseApiController {

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
    @DisplayName("profile real1 api 테스트")
    public void profile()  throws Exception {

        // given
        String[] profiles = {"real1"};
        BDDMockito.when(env.getActiveProfiles()).thenReturn(profiles);

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