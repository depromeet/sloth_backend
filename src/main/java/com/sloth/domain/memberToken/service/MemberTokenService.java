package com.sloth.domain.memberToken.service;

import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.exception.LessonNotFoundException;
import com.sloth.domain.member.Member;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.memberToken.repository.MemberTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberTokenService {

    private final MemberTokenRepository memberTokenRepository;

    public MemberToken findMemberTokenByMemberId(Long memberId) {
        return memberTokenRepository.findByMemberId(memberId);
    }

    public void deleteMemberToken(Long memberId) {
        MemberToken memberToken = memberTokenRepository.findByMemberId(memberId);
        memberTokenRepository.deleteById(memberToken.getMemberTokenId());
    }

}
