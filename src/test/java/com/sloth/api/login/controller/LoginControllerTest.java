package com.sloth.api.login.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sloth.api.BaseApiController;
import com.sloth.api.login.dto.FormJoinDto;
import com.sloth.api.login.dto.OauthRequestDto;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.SocialType;
import com.sloth.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;


    /*@Test
    @DisplayName("OAuth 로그인")
    void oauth_login() throws Exception {
        OauthRequestDto request = new OauthRequestDto();
        request.setSocialType(SocialType.GOOGLE.name());
    }*/

    @Test
    @DisplayName("폼 로그인 회원가입")
    void form_register() throws Exception {
        //given
        FormJoinDto request = new FormJoinDto("test","testformemail@email.com","testpassword","testpassword");

        //when
        mockMvc.perform(post("/api/form/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        //then
        Optional<Member> optionalMember = memberRepository.findByEmail("testformemail@email.com");
        assertTrue(optionalMember.isPresent());
        assertNotNull(optionalMember.get().getEmailConfirmCode());
        assertTrue(optionalMember.get().getEmailConfirmCodeCreatedAt().isBefore(LocalDateTime.now()));
        assertTrue(optionalMember.get().getEmailConfirmCodeCreatedAt().isAfter(LocalDateTime.now().minusMinutes(1)));
        assertFalse(optionalMember.get().isEmailConfirm());
    }

}