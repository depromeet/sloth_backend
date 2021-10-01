package com.sloth.domain.memberToken;

import com.sloth.domain.BaseEntity;
import com.sloth.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="member_token")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberToken extends BaseEntity {

    @Id
    @Column(name="member_token_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String refreshToken;

    private LocalDateTime tokenExpirationTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static MemberToken createMemberToken(Member member, String refreshToken, LocalDateTime tokenExpirationTime){
        return MemberToken.builder()
                .member(member)
                .refreshToken(refreshToken)
                .tokenExpirationTime(tokenExpirationTime)
                .build();
    }

}
