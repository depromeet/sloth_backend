package com.sloth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sloth.global.config.TestApiConfiguration;
import com.sloth.global.config.auth.TokenProvider;
import com.sloth.domain.member.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser(roles = "USER")
@ActiveProfiles("test")
@Import(TestApiConfiguration.class)
public class BaseApiController {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TokenProvider tokenProvider;

    @Autowired
    protected MemberRepository memberRepository;

    protected String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJhdWQiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MzcxNjM3NjUsImV4cCI6MTE2MzcxNjM3NjV9.9fwwJ6FC_36WwZi2AyAV1VY6SkVdyO6G7Mmr6B9MtSvy4SIwPyWl3G8qUjoZzy4g7gSpRqV-0kQBdB8t2Mm2Tw";

    protected final String testEmail = "email@email.com";

    protected String clientIdAccessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJhdWQiOiIxMjMxMjMxMjMiLCJpYXQiOjE2NDA4ODYyNjUsImV4cCI6MTc0MDg4NjI2NX0.R4L_GnNJAm7mziOyCdSJ35yGBhjsXEhPpGaXnkXfND3MI7lhwKAsDJjLrWNUB_L67B_p8blXkfFaLWueGoQp1g";

    protected final String testClientId = "123123123";

}
