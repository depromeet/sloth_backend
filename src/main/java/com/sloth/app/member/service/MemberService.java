package com.sloth.app.member.service;

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

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService{

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        boolean isExistMember = memberRepository.existsByEmail(member.getEmail());

        if(isExistMember){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(SecurityMember::new)
                .orElseThrow(() -> new BusinessException("존재하지 않는 계정입니다."));
    }

}