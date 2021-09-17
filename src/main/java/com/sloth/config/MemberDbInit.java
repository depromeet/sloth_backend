package com.sloth.config;

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
            memberFormDto.setAddress("test address" + i);
            memberFormDto.setEmail("test@naver.com" + i);
            memberFormDto.setName("test name" + i);
            memberFormDto.setPassword("test");
            Member member = Member.createAdmin(memberFormDto);
            memberRepository.save(member);
        }
    }

}
