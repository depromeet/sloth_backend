package com.sloth.global.config;

import com.sloth.domain.member.dto.MemberFormDto;
import com.sloth.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import com.sloth.domain.member.Member;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MemberDbInit {

    private final MemberRepository memberRepository;

    /**
     * 테스트 멤버 세팅
     */
    //@PostConstruct
    public void initMember() {
        for (int i = 0; i < 25; i++) {
            MemberFormDto formRequestDto = new MemberFormDto();
            formRequestDto.setEmail("testtest"+ + i + "@google.com");
            formRequestDto.setMemberName("test name" + i);
            formRequestDto.setPassword("test");
            Member member = Member.createAdmin(formRequestDto);
            memberRepository.save(member);
        }
    }

}
