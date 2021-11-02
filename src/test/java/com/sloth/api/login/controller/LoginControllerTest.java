package com.sloth.api.login.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sloth.api.BaseApiController;
import com.sloth.api.login.dto.FormJoinDto;
import com.sloth.api.login.dto.FormLoginRequestDto;
import com.sloth.api.login.dto.OauthRequestDto;
import com.sloth.api.login.dto.ResponseJwtTokenDto;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.SocialType;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.exception.ForbiddenException;
import com.sloth.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.meta.When;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager em;

    /*@Test
    @DisplayName("OAuth 로그인")
    void oauth_login() throws Exception {
        OauthRequestDto request = new OauthRequestDto();
        request.setSocialType(SocialType.GOOGLE.name());
    }*/

    @Test
    @DisplayName("폼 회원가입")
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

    @Test
    @DisplayName("폼 로그인 - 이메일 인증 안함")
    void form_login_notEmailConfirm() throws Exception {
        //when
        FormJoinDto formJoinDto = new FormJoinDto("name", "testformemail@email.com", "testpassword", "testpassword");
        Member member = Member.createFormMember(formJoinDto, passwordEncoder.encode(formJoinDto.getPassword()));
        memberRepository.save(member);

        FormLoginRequestDto request = new FormLoginRequestDto("testformemail@email.com", "testpassword");

        mockMvc.perform(post("/api/form/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("폼 로그인 - 이메일 인증 완료")
    @Transactional
    void form_login_EmailConfirm() throws Exception {
        //when
        FormJoinDto formJoinDto = new FormJoinDto("name", "testformemail@email.com", "testpassword", "testpassword");
        Member member = Member.createFormMember(formJoinDto, passwordEncoder.encode(formJoinDto.getPassword()));
        member.setEmailConfirm(true);
        memberRepository.save(member);

        FormLoginRequestDto request = new FormLoginRequestDto("testformemail@email.com", "testpassword");

        MvcResult mvcResult = mockMvc.perform(post("/api/form/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        MemberToken memberToken = member.getMemberToken();
        ResponseJwtTokenDto response = TestUtil.convert(mvcResult, ResponseJwtTokenDto.class);

        em.flush();
        em.clear();

        assertNotNull(memberToken);
        assertEquals(memberToken.getRefreshToken(), response.getRefreshToken()); // TODO 테스트 수정 및 고도화
    }

}