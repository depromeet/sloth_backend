package com.sloth.domain.memberToken.service;

import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.Member;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.memberToken.repository.MemberTokenRepository;
import com.sloth.test.base.BaseServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTokenServiceTest extends BaseServiceTest {

    @InjectMocks
    MemberTokenService memberTokenService;

    @Mock
    MemberTokenRepository memberTokenRepository;

    @Test
    @DisplayName("회원 아이디로 회원 토큰 조회")
    void findMemberTokenByMemberIdTest() {

        // given
        LocalDateTime tokenExpirationTime = LocalDateTime.of(2022,1,1,3,5,0);
        Member member = MemberCreator.createMember(1L, "test@test.com");
        MemberToken memberToken = MemberToken.createMemberToken(member, "abcabcabc", tokenExpirationTime);
        BDDMockito.when(memberTokenRepository.findByMemberId(1L)).thenReturn(memberToken);

        // when
        MemberToken resultMemberToken = memberTokenService.findMemberTokenByMemberId(member.getMemberId());

        // then
        assertThat(resultMemberToken.getMemberTokenId()).isEqualTo(memberToken.getMemberTokenId());
        assertThat(resultMemberToken.getRefreshToken()).isEqualTo(resultMemberToken.getRefreshToken());
        assertThat(resultMemberToken.getTokenExpirationTime()).isEqualTo(resultMemberToken.getTokenExpirationTime());
    }

}