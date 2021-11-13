package com.sloth.domain.member.service;

import com.sloth.api.login.dto.FormJoinDto;
import com.sloth.config.auth.dto.OAuthAttributes;
import com.sloth.config.auth.dto.TokenDto;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.nickname.Nickname;
import com.sloth.domain.nickname.service.NicknameService;
import com.sloth.exception.InvalidParameterException;
import com.sloth.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final NicknameService nicknameService;

    public void saveMember(OAuthAttributes oAuthAttributes, TokenDto tokenDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuthAttributes.getEmail());

        if(optionalMember.isEmpty()) {

            Nickname randomNickname = nicknameService.findRandomNickname();
            Member member = Member.createOauthMember(oAuthAttributes, randomNickname.getName());
            Member savedMember = memberRepository.save(member);
            randomNickname.updateUsed();

            //리프레시 토큰 저장
            saveRefreshToken(savedMember, tokenDto);
        }
    }

    public Member saveMember(FormJoinDto formRequestDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(formRequestDto.getEmail());
        if (optionalMember.isPresent()) {
            throw new InvalidParameterException("이미 존재하는 이메일입니다.");
        }

        Random random = new Random();
        String emailConfirmCode = String.valueOf(random.nextInt(899999) + 100000);
        Member member = Member.createFormMember(formRequestDto,
                                            passwordEncoder.encode(formRequestDto.getPassword()),
                                            emailConfirmCode);
        return memberRepository.save(member);
    }

    /**
     * refresh token 저장
     * @param tokenDto
     */
    public void saveRefreshToken(Member member, TokenDto tokenDto) {
        Date refreshTokenExpireTime = tokenDto.getRefreshTokenExpireTime();

        LocalDateTime tokenExpiredTime = DateTimeUtils.convertToLocalDateTime(refreshTokenExpireTime);

        MemberToken memberToken = MemberToken.createMemberToken(member, tokenDto.getRefreshToken(), tokenExpiredTime);
        member.updateMemberToken(memberToken);
        memberRepository.save(member);
    }

    public Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("해당 회원을 찾을 수 없습니다."));
    }

    public Member findByEmail (String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow( () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

}