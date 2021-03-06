package com.sloth.domain.fcm.repository;

import com.sloth.domain.fcm.entity.FcmToken;
import com.sloth.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    FcmToken findByMemberAndFcmToken(Member member, String fcmToken);

}
