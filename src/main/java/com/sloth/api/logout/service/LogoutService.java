package com.sloth.api.logout.service;

import com.sloth.domain.fcm.entity.FcmToken;
import com.sloth.domain.fcm.service.FcmTokenService;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.memberToken.service.MemberTokenService;
import com.sloth.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService {

    private final MemberService memberService;
    private final MemberTokenService memberTokenService;
    private final FcmTokenService fcmTokenService;

    public void logout(String email, LocalDateTime now, String fcmToken) {
        Member member = memberService.findByEmail(email);
        MemberToken memberToken = memberTokenService.findMemberTokenByMemberId(member.getMemberId());
        memberToken.expireToken(now);

        if(StringUtils.hasText(fcmToken)) {
            fcmTokenService.deleteFcmToken(fcmToken);
        }
    }

}
