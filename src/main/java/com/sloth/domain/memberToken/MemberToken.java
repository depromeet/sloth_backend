package com.sloth.domain.memberToken;

import com.sloth.domain.common.BaseEntity;
import com.sloth.domain.member.Member;
import com.sloth.domain.memberToken.constant.TokenRefreshCritnTime;
import com.sloth.domain.memberToken.constant.MemberTokenType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name="member_token")
@Getter
@ToString(exclude = "member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberToken extends BaseEntity {

    @Id
    @Column(name="member_token_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberTokenId;

    private String token;

    private LocalDateTime tokenExpirationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private MemberTokenType memberTokenType;

    public static MemberToken createMemberToken(Member member, String token, LocalDateTime tokenExpirationTime, MemberTokenType memberTokenType){
        MemberToken memberToken = MemberToken.builder()
                .member(member)
                .token(token)
                .tokenExpirationTime(tokenExpirationTime)
                .memberTokenType(memberTokenType)
                .build();

        member.updateLoginRefreshToken(memberToken);

        return memberToken;
    }

    /**
     * 토큰 만료 시간 갱신
     * @param tokenExpirationTime
     */
    public void updateTokenExpirationTime(LocalDateTime tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }

    /**
     * 리프레시 토큰이 만료 갱신 기준 시간 이하일 경우 만료 시간 갱신
     * @param now
     * @param tokenRefreshCritnTime 해당 시간 이하일 경우 토큰 만료 시간 갱신
     */
    public void updateRefreshTokenExpireTime(LocalDateTime now, TokenRefreshCritnTime tokenRefreshCritnTime) {
        long hours = ChronoUnit.HOURS.between(now, tokenExpirationTime);
        if(hours <= tokenRefreshCritnTime.getRefreshCritnTime()) {
            updateTokenExpirationTime(now.plusWeeks(2));
        }
    }

    public void expireToken(LocalDateTime now) {
        if(tokenExpirationTime.isAfter(now)) {
            this.tokenExpirationTime = now;
        }
    }

    public void updateLoginRefreshToken(MemberToken updateRefreshMemberToken) {
        this.token = updateRefreshMemberToken.getToken();
    }
}
