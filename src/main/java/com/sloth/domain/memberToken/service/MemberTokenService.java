package com.sloth.domain.memberToken.service;

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

}
