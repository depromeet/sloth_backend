package com.sloth.domain.fcm.service;

import com.sloth.domain.fcm.entity.FcmToken;
import com.sloth.domain.fcm.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;

    public FcmToken findByMemberIdAndFcmToken(Long memberId, String fcmToken) {
        return fcmTokenRepository.findByMemberIdAndFcmToken(memberId, fcmToken);
    }

    public FcmToken saveFcmToken(FcmToken fcmToken) {
        return fcmTokenRepository.save(fcmToken);
    }
}
