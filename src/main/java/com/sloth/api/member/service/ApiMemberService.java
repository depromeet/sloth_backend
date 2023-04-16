package com.sloth.api.member.service;

import com.sloth.api.member.dto.MemberInfoDto;
import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.domain.fcm.entity.FcmToken;
import com.sloth.domain.fcm.service.FcmTokenService;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.domain.memberToken.service.MemberTokenService;
import com.sloth.infra.google.GoogleCloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApiMemberService {

    private final MemberService memberService;
    private final FcmTokenService fcmTokenService;
    private final MemberTokenService memberTokenService;
    private final GoogleCloudStorageService googleCloudStorageService;

    @Value("${google-cloud.profile-storage-url}")
    private String profileStorageUrl;

    @Value("${google-cloud.profile-bucket-name}")
    private String profileBucketName;

    public MemberUpdateDto.Response updateMemberInfo(String email, MultipartFile profileImage, MemberUpdateDto.Request requestDto) {

        // 회원 이름 수정
        Member member = memberService.findByEmail(email);
        member.updateMemberName(requestDto.getMemberName());

        // 프로필 이미지 업데이트
        if(profileImage != null && !profileImage.isEmpty()) {
            String uploadImageFileName = googleCloudStorageService.uploadImageFileToGCS(profileImage, profileBucketName);
            member.updateProfileImage(uploadImageFileName);
        }

        String profileImageUrl = profileStorageUrl + member.getPicture();
        return MemberUpdateDto.Response.builder()
                .memberName(member.getMemberName())
                .profileImageUrl(profileImageUrl)
                .build();
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
            return new MemberInfoDto(member, profileStorageUrl, isPushAlarmUse);
        }

        return new MemberInfoDto(member, profileStorageUrl, isPushAlarmUse);
    }

    public void deleteMember(String email) {
        Member member = memberService.findByEmail(email);
        //리프레시토큰 fcm 토큰 삭제
        List<FcmToken> fcmTokens = fcmTokenService.findByMember(member);
        fcmTokens.stream().forEach(fcmToken -> fcmTokenService.deleteFcmToken(fcmToken.getFcmToken()));
        memberTokenService.deleteMemberToken(member.getMemberId());

        memberService.deleteMember(member);
    }
}
