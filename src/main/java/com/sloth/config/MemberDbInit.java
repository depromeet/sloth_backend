package com.sloth.config;

import com.sloth.api.oauth.dto.SocialType;
import com.sloth.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.dto.MemberFormDto;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class MemberDbInit {

    private final MemberRepository memberRepository;

    /**
     * 테스트 멤버 세팅
     */
    @PostConstruct
    public void initMember() {
        for (int i = 0; i < 25; i++) {
            MemberFormDto memberFormDto = new MemberFormDto();
            memberFormDto.setEmail("testtest"+ + i + "@google.com");
            memberFormDto.setName("test name" + i);
            memberFormDto.setPassword("test");
            memberFormDto.setSocialType(SocialType.GOOGLE);
            Member member = Member.createAdmin(memberFormDto);
            memberRepository.save(member);
        }
    }

}
