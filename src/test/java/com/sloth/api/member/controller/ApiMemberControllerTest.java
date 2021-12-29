package com.sloth.api.member.controller;

import com.sloth.api.BaseApiController;
import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.api.member.service.ApiMemberService;
import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Execution(ExecutionMode.CONCURRENT)
@WebMvcTest(controllers = ApiMemberController.class)
public class ApiMemberControllerTest extends BaseApiController {

    @MockBean
    private ApiMemberService memberService;

    @Test
    @DisplayName("회원 정보 수정 API 테스트")
    public void updateMemberInfo() throws Exception {

        // given
        MemberUpdateDto.Request request = new MemberUpdateDto.Request();
        request.setMemberName("회원 정보 수정");

        Optional<Member> optionalMember = Optional.of(MemberCreator.createStubMember());
        given(memberRepository.findByEmail(testEmail))
                .willReturn(optionalMember);
        Member member = optionalMember.get();

        Member updateMember = MemberCreator.createStubMember();
        updateMember.updateMemberName(request.getMemberName());

        given(memberService.updateMemberInfo(eq(member), any(MemberUpdateDto.Request.class)))
                .willReturn(updateMember);

        // when
        ResultActions result = mockMvc.perform(patch("/api/member")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                ;

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.memberName", is(request.getMemberName())))
                ;
    }

    @Test
    @DisplayName("회원 정보 없을 경우 테스트")
    public void updateMemberInfoNotExists() throws Exception {

        // given
        MemberUpdateDto.Request request = new MemberUpdateDto.Request();

        Optional<Member> optionalMember = Optional.of(MemberCreator.createStubMember());
        given(memberRepository.findByEmail(testEmail))
                .willReturn(optionalMember);
        Member member = optionalMember.get();

        Member updateMember = MemberCreator.createStubMember();
        updateMember.updateMemberName(request.getMemberName());

        given(memberService.updateMemberInfo(eq(member), any(MemberUpdateDto.Request.class)))
                .willReturn(updateMember);

        // when
        ResultActions result = mockMvc.perform(patch("/api/member")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                ;

        // then
        result.andExpect(MockMvcResultMatchers.status().is4xxClientError())
        ;
    }
}