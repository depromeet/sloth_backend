package com.sloth.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sloth.app.member.service.CustomOAuth2UserService;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.memberToken.repository.MemberTokenRepository;
import com.sloth.global.config.WebConfig;
import com.sloth.global.config.auth.TokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(WebConfig.class)
public class TestApiConfiguration {

    @MockBean
    private MemberTokenRepository memberTokenRepository;

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    private MemberRepository memberRepository;

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
