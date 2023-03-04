package com.sloth.domain.fcm.repository;

import com.sloth.domain.fcm.entity.FcmToken;
import com.sloth.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    FcmToken findByMemberAndFcmToken(Member member, String fcmToken);

    FcmToken findByMemberAndDeviceId(Member member, String deviceId);

    List<FcmToken> findByMember(Member member);

    @Modifying(clearAutomatically = true)
    @Query("delete from FcmToken ft where ft.fcmToken = :fcmToken")
    void deleteByFcmToken(@Param("fcmToken") String fcmToken);

}
