package com.sloth.app.member.service;

import com.sloth.api.oauth.dto.SocialType;
import com.sloth.config.auth.dto.OAuthAttributes;
import com.sloth.config.auth.dto.TokenDto;
import com.sloth.domain.member.dto.MemberFormDto;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import com.sloth.domain.member.model.SecurityMember;
import com.sloth.domain.member.Member;
import com.sloth.exception.BusinessException;
import com.sloth.domain.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(OAuthAttributes oAuthAttributes, TokenDto tokenDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuthAttributes.getEmail());
        Member member = null;

        if(optionalMember.isEmpty()) {
            member = Member.createOauthMember(oAuthAttributes);
            Member savedMember = memberRepository.save(member);

            //리프레시 토큰 저장
            saveRefreshToken(savedMember, tokenDto);
        }

        return member;
    }

    /**
     * refresh token 저장
     * @param tokenDto
     */
    private void saveRefreshToken(Member member, TokenDto tokenDto) {
        Date refreshTokenExpireTime = tokenDto.getRefreshTokenExpireTime();

        LocalDateTime tokenExpiredTime = DateTimeUtils.convertToLocalDateTime(refreshTokenExpireTime);

        MemberToken memberToken = MemberToken.createMemberToken(member, tokenDto.getRefreshToken(), tokenExpiredTime);
        member.setMemberToken(memberToken);
        memberRepository.save(member);
    }

}