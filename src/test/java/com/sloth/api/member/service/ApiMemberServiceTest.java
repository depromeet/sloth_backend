package com.sloth.api.member.service;

import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.test.base.BaseServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.BDDMockito.given;

public class ApiMemberServiceTest extends BaseServiceTest {

    @InjectMocks
    private ApiMemberService apiMemberService;

    @Mock
    private MemberService memberService;

    @Test
    @DisplayName("회원 정보 업데이트 테스트")
    public void updateMemberInfoTest() {

        // given
        MemberUpdateDto.Request request = new MemberUpdateDto.Request();
        request.setMemberName("회원 정보 수정");

        Member member = MemberCreator.createStubMember("email@email.com");

        // when
        given(memberService.findByEmail(member.getEmail())).willReturn(member);
        apiMemberService.updateMemberInfo(member.getEmail(), request);

        // then
        Assertions.assertThat(member.getMemberName()).isEqualTo(request.getMemberName());

    }

}