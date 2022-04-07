package com.sloth.api.logout.service;

import com.sloth.api.logout.service.LogoutService;
import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.memberToken.service.MemberTokenService;
import com.sloth.test.base.BaseServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.*;

class LogoutServiceTest extends BaseServiceTest {

    @InjectMocks
    private LogoutService logoutService;

    @Mock
    private MemberService memberService;

    @Mock
    private MemberTokenService memberTokenService;

    @Test
    @DisplayName("로그아웃 테스트")
    void logoutTest() {

        // given
        String email = "test@test.com";
        LocalDateTime tokenExpirationTime = LocalDateTime.of(2022,1,1,3,5,0);
        Member member = MemberCreator.createMember(1L, email);
        MemberToken memberToken = MemberToken.createMemberToken(member, "abcabcabc", tokenExpirationTime);
        when(memberService.findByEmail(email)).thenReturn(member);
        when(memberTokenService.findMemberTokenByMemberId(member.getMemberId())).thenReturn(memberToken);

        // when
        LocalDateTime now = LocalDateTime.of(2021,12,20,3,5,0);
        logoutService.logout(email, now);

        // then
        Assertions.assertThat(memberToken.getTokenExpirationTime()).isEqualTo(now);
    }

}