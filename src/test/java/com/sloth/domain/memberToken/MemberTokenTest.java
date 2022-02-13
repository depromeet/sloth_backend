package com.sloth.domain.memberToken;

import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.Member;
import com.sloth.domain.memberToken.constant.MemberTokenType;
import com.sloth.domain.memberToken.constant.TokenRefreshCritnTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class MemberTokenTest {

    private Member member;
    private LocalDateTime now;
    private MemberToken memberToken;

    @BeforeEach
    public void init() {
        member = MemberCreator.createMember(1L, "test@test.com");
        now = LocalDateTime.of(2022,01,28,0,0);
        memberToken = MemberToken.createMemberToken(member, "refreshToken", now, MemberTokenType.LOGIN_REFRESH);
    }

    @Test
    @DisplayName("토큰 만료 시간 갱신 테스트")
    public void updateTokenExpirationTimeTest() {

        LocalDateTime tokenExpirationTime = LocalDateTime.of(2022,01,26,0,0);

        // when
        memberToken.updateTokenExpirationTime(tokenExpirationTime);

        // then
        Assertions.assertThat(memberToken.getTokenExpirationTime()).isEqualTo(tokenExpirationTime);

    }

    @Test
    @DisplayName("리프레시 토큰이 만료 갱신 기준 시간 이하일 경우 만료 시간 갱신 테스트")
    public void updateRefreshTokenExpireTimeTest() {

        // when
        memberToken.updateRefreshTokenExpireTime(now, TokenRefreshCritnTime.HOURS_72);

        // then
        Assertions.assertThat(memberToken.getTokenExpirationTime()).isEqualTo(now.plusWeeks(2));

    }

    @Test
    @DisplayName("리프레시 토큰이 만료 갱신 기준 시간보다 클 경우 테스트")
    public void updateRefreshTokenNotExpireTimeTest() {

        // when
        LocalDateTime tokenExpirationTime = memberToken.getTokenExpirationTime();
        now = LocalDateTime.of(2022,01,23,0,0);
        memberToken.updateRefreshTokenExpireTime(now, TokenRefreshCritnTime.HOURS_72);

        // then
        Assertions.assertThat(memberToken.getTokenExpirationTime()).isEqualTo(tokenExpirationTime);

    }

    @Test
    @DisplayName("토큰 만료 테스트")
    public void expireTokenTest() {

        // given
        LocalDateTime now = LocalDateTime.of(2022,1,1,2,5,0);

        // when
        memberToken.expireToken(now);

        // then
        Assertions.assertThat(memberToken.getTokenExpirationTime()).isEqualTo(now);

    }

}