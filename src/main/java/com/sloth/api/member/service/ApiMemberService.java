package com.sloth.api.member.service;

import com.sloth.api.member.dto.MemberInfoDto;
import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.config.auth.TokenProvider;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ApiMemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public MemberInfoDto findByEmail (String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow( () -> new UsernameNotFoundException("해당 회원 정보가 없습니다. email : " + email));

        return new MemberInfoDto(member);
    }

    @Transactional
    public Long update(Member member, MemberUpdateDto requestDto) {
        member.update(requestDto.getMemberName());

        return member.getMemberId();
    }

    public MemberInfoDto getMemberInfo(String token) {
        Claims tokenClaims = tokenProvider.getTokenClaims(token);
        String email = tokenClaims.getAudience();

        return findByEmail(email);
    }
}
