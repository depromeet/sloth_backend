package com.sloth.api.member.service;

import com.sloth.api.member.dto.MemberInfoDto;
import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.domain.fcm.entity.FcmToken;
import com.sloth.domain.fcm.service.FcmTokenService;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApiMemberService {

    private final MemberService memberService;
    private final FcmTokenService fcmTokenService;

    public Member updateMemberInfo(String email, MemberUpdateDto.Request requestDto) {
        Member member = memberService.findByEmail(email);
        member.updateMemberName(requestDto.getMemberName());
        return member;
    }

    @Transactional(readOnly = true)
    public MemberInfoDto findMemberInfoDto(String email) {
        Member member = memberService.findByEmail(email);
        List<FcmToken> fcmTokens = fcmTokenService.findByMember(member);
        Boolean isPushAlarmUse = false;

        if(fcmTokens != null) {
            Optional<FcmToken> fcmToken = fcmTokens.stream().findFirst();
            if(fcmToken.isPresent() && fcmToken.get().getIsUse()) {
                isPushAlarmUse = true;
            } else {
                isPushAlarmUse = false;
            }
            return new MemberInfoDto(member, isPushAlarmUse);
        }

        return new MemberInfoDto(member, isPushAlarmUse);
    }

}
