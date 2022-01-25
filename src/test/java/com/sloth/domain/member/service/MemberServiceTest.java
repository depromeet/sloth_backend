package com.sloth.domain.member.service;

import com.sloth.api.login.dto.EmailConfirmResendRequestDto;
import com.sloth.api.login.dto.FormJoinDto;
import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.SocialType;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.nickname.Nickname;
import com.sloth.domain.nickname.service.NicknameService;
import com.sloth.global.config.auth.TokenProvider;
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
import java.util.Date;
import java.util.HashMap;
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
                .willReturn(Optional.empty());

        // when & then
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            memberService.findMember(memberId);
        });

        assertEquals("해당 회원을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("이메일 회원 조회 테스트 - 성공")
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
    @DisplayName("이메일 회원 조회 테스트 - 실패")
    void findByEmail_fail() {
        // given
        String email = "email@email.com";
        given(memberRepository.findByEmail(email))
                .willReturn(Optional.empty());

        // when & then
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            memberService.findByEmail(email);
        });

        assertEquals("해당 회원이 존재하지 않습니다.", exception.getMessage());
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

    @Test
    @DisplayName("소셜 멤버 생성 테스트")
    void createOauthMemberTest() {
        //given
        Nickname nickname = Nickname.builder()
                .nicknameId(1L)
                .name("테스트용 닉네임")
                .isUsed(false)
                .build();
        given(nicknameService.findRandomNickname()).willReturn(nickname);

        String email = "test@test.com";
        SocialType socialType = SocialType.KAKAO;
        String password = "password";
        OAuthAttributes oAuthAttributes = new OAuthAttributes(new HashMap<>(), "test", "test", email, socialType, password);

        //when
        Member oauthMember = memberService.createOauthMember(oAuthAttributes);

        //then
        assertEquals(email, oauthMember.getEmail());
        assertEquals(password, oauthMember.getPassword());
        assertEquals(socialType, oauthMember.getSocialType());
        assertEquals(nickname.getName(), oauthMember.getMemberName());
        assertTrue(nickname.isUsed());
    }

    @Test
    @DisplayName("email로 Optional 멤버 조회 테스트")
    void getOptionalMemberTest() {
        //given
        String email = "email@email.com";
        Member stubMember = MemberCreator.createStubMember(email);
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(stubMember));

        //when
        Optional<Member> optionalMember = memberService.getOptionalMember(email);

        //then
        assertEquals(Optional.class, optionalMember.getClass());
        assertEquals(Optional.of(stubMember), optionalMember);
        assertEquals(email, optionalMember.get().getEmail());
    }

    @Test
    @DisplayName("멤버 저장 테스트") // TODO memberService.saveRefreshToken mocking..??
    void saveMemberTest() {
        //given
        String email = "email@email.com";
        Member stubMember = MemberCreator.createStubMember(email);
        Date expireTime = new Date(Long.parseLong("99999999999"));
        TokenDto tokenDto = new TokenDto("grantType", "access", expireTime, "refresh", expireTime);
        given(memberRepository.save(stubMember)).willReturn(stubMember);

        //when
        Member member = memberService.saveMember(stubMember, tokenDto);

        //then
        assertEquals(email, member.getEmail());
    }
}