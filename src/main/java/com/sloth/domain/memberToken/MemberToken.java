package com.sloth.domain.memberToken;

import com.sloth.domain.BaseEntity;
import com.sloth.domain.member.Member;
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

    private String refreshToken;

    private LocalDateTime tokenExpirationTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static MemberToken createMemberToken(Member member, String refreshToken, LocalDateTime tokenExpirationTime){
        MemberToken memberToken = MemberToken.builder()
                .member(member)
                .refreshToken(refreshToken)
                .tokenExpirationTime(tokenExpirationTime)
                .build();

        member.updateMemberToken(memberToken);

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
     * @param token
     */
    public void updateRefreshTokenExpireTime(String token) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), tokenExpirationTime);
        if(hours <= 72) {   //토큰 만료 시간이 72시간 이하일 경우 토큰 만료 시간 갱신
            updateTokenExpirationTime(LocalDateTime.now().plusWeeks(2));
        }
    }

}
