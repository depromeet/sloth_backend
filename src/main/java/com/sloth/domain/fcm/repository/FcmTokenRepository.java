package com.sloth.domain.fcm.repository;

import com.sloth.domain.fcm.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    FcmToken findByMemberIdAndFcmToken(Long memberId, String fcmToken);

}
