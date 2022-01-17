package com.sloth.domain.member.service;

import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.nickname.service.NicknameService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
    @DisplayName("memberId 기반 회원 조회 테스트")
    void findMember() {

        // given
        Long memberId = 1L;
        String email = "test@test.com";
        Member member = MemberCreator.createStubMember(email);
        Optional<Member> optionalMember = Optional.of(member);
        BDDMockito.given(memberRepository.findById(memberId))
                .willReturn(optionalMember);

        // when
        Member savedMember = memberService.findMember(memberId);

        // then
        Assertions.assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
        Assertions.assertThat(savedMember.getMemberName()).isEqualTo(member.getMemberName());
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

}