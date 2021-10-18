package com.sloth.api.member.service;

import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.api.member.dto.ResponseMemberDto;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ApiMemberService {

    private final MemberRepository memberRepository;

    public ResponseMemberDto findById (Long id) {
        Member member = memberRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("해당 정보가 없습니다. id =" + id));

        return  new ResponseMemberDto(member);
    }

    @Transactional
    public Long update(Long id, MemberUpdateDto requestDto) {

        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 정보가 없습니다. id =" + id));

        member.update(requestDto.getName());

        return id;
    }
}
