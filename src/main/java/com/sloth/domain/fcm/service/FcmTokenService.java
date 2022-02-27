package com.sloth.domain.fcm.service;

import com.sloth.domain.fcm.entity.FcmToken;
import com.sloth.domain.fcm.repository.FcmTokenRepository;
import com.sloth.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;

    public FcmToken findByMemberAndFcmToken(Member member, String fcmToken) {
        return fcmTokenRepository.findByMemberAndFcmToken(member, fcmToken);
    }

    public FcmToken saveFcmToken(FcmToken fcmToken) {
        return fcmTokenRepository.save(fcmToken);
    }
}
