package com.sloth.api.member.controller;

import com.sloth.api.BaseApiController;
import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.api.member.service.ApiMemberService;
import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Execution(ExecutionMode.CONCURRENT)
@WebMvcTest(controllers = ApiMemberController.class)
public class ApiMemberControllerTest extends BaseApiController {

    private Member member;

    @MockBean
    private ApiMemberService memberService;

    @BeforeEach
    public void init() {
        Optional<Member> optionalMember = Optional.of(MemberCreator.createStubMember(testEmail));
        given(memberRepository.findByEmail(testEmail))
                .willReturn(optionalMember);
        member = optionalMember.get();
    }

    @Test
    @DisplayName("회원 정보 수정 API 테스트")
    public void updateMemberInfo() throws Exception {

        // given
        MemberUpdateDto.Request request = new MemberUpdateDto.Request();
        request.setMemberName("회원 정보 수정");

        Member updateMember = MemberCreator.createStubMember(testEmail);
        updateMember.updateMemberName(request.getMemberName());

        given(memberService.updateMemberInfo(eq(member), any(MemberUpdateDto.Request.class)))
                .willReturn(updateMember);

        // when
        ResultActions result = mockMvc.perform(patch("/api/member")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.memberName", is(request.getMemberName())));
    }

    @Test
    @DisplayName("회원 정보 수정 API 테스트 - 잘못된 데이터 요청할 경우")
    public void updateMemberInfoFailTest() throws Exception {

        // given
        MemberUpdateDto.Request request = new MemberUpdateDto.Request();

        Member updateMember = MemberCreator.createStubMember(testEmail);
        updateMember.updateMemberName(request.getMemberName());

        given(memberService.updateMemberInfo(eq(member), any(MemberUpdateDto.Request.class)))
                .willReturn(updateMember);

        // when
        ResultActions result = mockMvc.perform(patch("/api/member")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 정보 없을 경우 테스트")
    public void updateMemberInfoNotExists() throws Exception {

        // given
        MemberUpdateDto.Request request = new MemberUpdateDto.Request();

        // when
        ResultActions result = mockMvc.perform(patch("/api/member")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                ;

        // then
        result.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("이메일 제공 동의 안한 회원 정보 조회")
    public void getMemberInfo_not_agree_email() throws Exception{
        //given
        Optional<Member> noEmailMember = Optional.of(MemberCreator.createNoEmailMember(testClientId));
        given(memberRepository.findByEmail(noEmailMember.get().getEmail())).willReturn(noEmailMember);
        Member member = noEmailMember.get();

        //when
        ResultActions result = mockMvc.perform(get("/api/member")
                .header(HttpHeaders.AUTHORIZATION, this.clientIdAccessToken)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("memberId").value(equalTo(member.getMemberId())))
                .andExpect(jsonPath("memberName").value(equalTo(member.getMemberName())))
                .andExpect(jsonPath("email").value(equalTo(member.getEmail())))
                .andExpect(jsonPath("isEmailProvided").value(equalTo(false)));

    }

    @Test
    @DisplayName("이메일 제공 동의 회원 정보 조회")
    public void getMemberInfo_agree_email() throws Exception{

        //when
        ResultActions result = mockMvc.perform(get("/api/member")
                .header(HttpHeaders.AUTHORIZATION, this.accessToken)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("memberId").value(equalTo(member.getMemberId())))
                .andExpect(jsonPath("memberName").value(equalTo(member.getMemberName())))
                .andExpect(jsonPath("email").value(equalTo(member.getEmail())))
                .andExpect(jsonPath("isEmailProvided").value(equalTo(true)));

    }
}