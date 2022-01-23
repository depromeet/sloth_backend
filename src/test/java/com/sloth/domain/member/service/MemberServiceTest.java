package com.sloth.domain.member.service;

import com.sloth.api.login.dto.EmailConfirmResendRequestDto;
import com.sloth.api.login.dto.FormJoinDto;
import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.SocialType;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.nickname.Nickname;
import com.sloth.domain.nickname.service.NicknameService;
import com.sloth.global.config.auth.dto.OAuthAttributes;
import com.sloth.global.config.auth.dto.TokenDto;
import com.sloth.global.exception.InvalidParameterException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NicknameService nicknameService;

    @Test
    @DisplayName("memberId 기반 회원 조회 테스트 - 성공")
    void findMember() {

        // given
        Long memberId = 1L;
        String email = "test@test.com";
        Member member = MemberCreator.createStubMember(email);
        Optional<Member> optionalMember = Optional.of(member);
        given(memberRepository.findById(memberId))
                .willReturn(optionalMember);

        // when
        Member savedMember = memberService.findMember(memberId);

        // then
        Assertions.assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
        Assertions.assertThat(savedMember.getMemberName()).isEqualTo(member.getMemberName());
    }

    @Test
    @DisplayName("memberId 기반 회원 조회 테스트 - 실패")
    void findMember_fail() {
        // given
        Long memberId = 1L;
        given(memberRepository.findById(memberId))
                .willThrow(new UsernameNotFoundException("해당 회원을 찾을 수 없습니다."));

        // when & then
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            memberService.findMember(memberId);
        });

        assertEquals("해당 회원을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("이메일 회원 조회 테스트")
    void findByEmail() {

        // given
        String email = "test@test.com";
        Member member = MemberCreator.createStubMember(email);
        Optional<Member> optionalMember = Optional.of(member);
        given(memberRepository.findByEmail(email))
                .willReturn(optionalMember);
        // when
        Member savedMember = memberService.findByEmail(email);

        // then
        Assertions.assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("존재하는 이메일로 회원가입 시 실패")
    void form_join_duplicated_email() {
        //given
        String email = "test@email.com";
        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(MemberCreator.createStubMember(email)));

        //when & then
        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            memberService.saveMember(new FormJoinDto("test", email, "password", "password"));
        });

        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("잘못된 패스워드 입력시 실패")
    void check_password_fail() {
        //given
        String email = "email@email.com";
        String password = "password";
        String invalidPassword = "invalidPassword";
        Optional<Member> optionalMember = Optional.of(MemberCreator.createEmailPasswordMember(email, password));

        given(memberRepository.findByEmail(email))
                .willReturn(optionalMember);

        given(passwordEncoder.matches(invalidPassword, password))
                .willReturn(false);

        //when
        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            memberService.updateConfirmEmailCode(new EmailConfirmResendRequestDto(email, invalidPassword));
        });

        //then
        assertEquals("회원 정보가 옳지 않습니다.", exception.getMessage());
    }
}