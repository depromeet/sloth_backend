package com.sloth.api.member.service;

import com.sloth.api.member.dto.MemberInfoDto;
import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.domain.fcm.entity.FcmToken;
import com.sloth.domain.fcm.service.FcmTokenService;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.domain.memberToken.service.MemberTokenService;
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
    private final MemberTokenService memberTokenService;

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

    public void deleteMember(String email, String password) {
        Member member = memberService.findByEmail(email);
        //리프레시토큰 fcm 토큰 삭제
        List<FcmToken> fcmTokens = fcmTokenService.findByMember(member);
        fcmTokens.stream().forEach(fcmToken -> fcmTokenService.deleteFcmToken(fcmToken.getFcmToken()));
        memberTokenService.deleteMemberToken(member.getMemberId());

        memberService.deleteMember(member, password);
    }
}
