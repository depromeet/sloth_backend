package com.sloth.domain.member.service;

import com.sloth.api.login.dto.EmailConfirmResendRequestDto;
import com.sloth.api.login.dto.FormJoinDto;
import com.sloth.global.config.auth.dto.OAuthAttributes;
import com.sloth.global.config.auth.dto.TokenDto;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.nickname.Nickname;
import com.sloth.domain.nickname.service.NicknameService;
import com.sloth.global.exception.ForbiddenException;
import com.sloth.global.exception.InvalidParameterException;
import com.sloth.global.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final NicknameService nicknameService;

    public Boolean saveMember(OAuthAttributes oAuthAttributes, TokenDto tokenDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuthAttributes.getEmail());
        Member savedMember;
        Boolean isNewMember = false;
        if(optionalMember.isEmpty()) {
            Member member = createOauthMember(oAuthAttributes);

            savedMember = memberRepository.save(member);
            isNewMember = true;
        } else {
            savedMember = optionalMember.get();
        }

        saveRefreshToken(savedMember, tokenDto);
        return isNewMember;
    }

    private Member createOauthMember(OAuthAttributes oAuthAttributes) {
        Nickname randomNickname = nicknameService.findRandomNickname();
        Member member = Member.createOauthMember(oAuthAttributes, randomNickname.getName());
        randomNickname.updateUsed();
        return member;
    }

    public Member saveMember(FormJoinDto formRequestDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(formRequestDto.getEmail());
        if (optionalMember.isPresent()) {
            throw new InvalidParameterException("이미 존재하는 이메일입니다.");
        }

        Member member = Member.createFormMember(formRequestDto,
                                            passwordEncoder.encode(formRequestDto.getPassword()));
        createEmailConfirmCode(member);
        return memberRepository.save(member);
    }

    private void createEmailConfirmCode(Member member) {
        Random random = new Random();
        String newConfirmEmailCode = String.valueOf(random.nextInt(899999) + 100000);
        member.updateConfirmEmailCode(newConfirmEmailCode, LocalDateTime.now());
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
                .orElseThrow( () -> new UsernameNotFoundException("해당 회원이 존재하지 않습니다."));
    }

    public Member updateConfirmEmailCode(EmailConfirmResendRequestDto requestDto) {
        Member member = findByEmail(requestDto.getEmail());
        checkPassword(member, requestDto.getPassword());
        if (!member.canCreateEmailConfirmCode()) {
            throw new ForbiddenException("이메일 발송 5분 후에 재전송을 할 수 있습니다.");
        }
        createEmailConfirmCode(member);
        return member;
    }

    private void checkPassword(Member member, String rawPassword) {
        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new InvalidParameterException("회원 정보가 옳지 않습니다.");
        }
    }
}