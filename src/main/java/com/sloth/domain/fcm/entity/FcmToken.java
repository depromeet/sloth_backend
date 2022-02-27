package com.sloth.domain.fcm.entity;

import com.sloth.domain.common.BaseEntity;
import com.sloth.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "fcm_token")
public class FcmToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fcmTokenId;

    private String fcmToken;

    private Boolean isUse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static FcmToken createFcmToken(Member member, String token) {
        FcmToken fcmToken = new FcmToken();
        fcmToken.member = member;
        fcmToken.fcmToken = token;
        fcmToken.isUse = true;
        return fcmToken;
    }

    public void updateIsUse(boolean isUse) {
        this.isUse = isUse;
    }

}
