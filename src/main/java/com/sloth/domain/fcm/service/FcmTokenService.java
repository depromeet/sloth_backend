package com.sloth.domain.fcm.service;

import com.sloth.domain.fcm.entity.FcmToken;
import com.sloth.domain.fcm.repository.FcmTokenRepository;
import com.sloth.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;

    public FcmToken findByMemberAndFcmToken(Member member, String fcmToken) {
        return fcmTokenRepository.findByMemberAndFcmToken(member, fcmToken);
    }

    public FcmToken findByMemberAndDeviceId(Member member, String deviceId) {
        return fcmTokenRepository.findByMemberAndDeviceId(member, deviceId);
    }

    public FcmToken saveFcmToken(FcmToken fcmToken) {
        return fcmTokenRepository.save(fcmToken);
    }

    public List<FcmToken> findByMember(Member member) {
        return fcmTokenRepository.findByMember(member);
    }

    public void deleteFcmToken(String fcmToken) {
        fcmTokenRepository.deleteByFcmToken(fcmToken);
    }

}
