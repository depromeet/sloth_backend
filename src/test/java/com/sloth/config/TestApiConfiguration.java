package com.sloth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sloth.app.member.service.CustomOAuth2UserService;
import com.sloth.config.auth.TokenProvider;
import com.sloth.domain.memberToken.repository.MemberTokenRepository;
import com.sloth.resolver.CurrentMemberArgumentResolver;
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
    private CurrentMemberArgumentResolver currentMemberArgumentResolver;

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

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