package com.sloth.domain.member;

import com.sloth.api.login.form.dto.FormJoinDto;
import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.constant.Role;
import com.sloth.domain.member.constant.SocialType;
import com.sloth.domain.member.dto.MemberFormDto;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.global.config.auth.dto.OAuthAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class MemberTest {
    private Member testMember;

    @BeforeEach
    public void beforeEach() {
        testMember = MemberCreator.createStubMember("testEmail@email.com");
    }

    @Test
    @DisplayName("Admin 멤버 생성")
    void createAdmin() {
        // given
        String name = "이름";
        String email = "email@email.com";
        String password = "password";
        MemberFormDto memberFormDto = MemberFormDto.builder()
                .memberName(name)
                .email(email)
                .password(password)
                .socialType(SocialType.KAKAO)
                .build();

        // when
        Member admin = Member.createAdmin(memberFormDto);

        // then
        assertEquals(email, admin.getEmail());
        assertEquals(name, admin.getMemberName());
        assertEquals(SocialType.KAKAO, admin.getSocialType());
        assertEquals(password, admin.getPassword());
        assertNotNull(admin.getLessons());
        assertTrue(admin.isEmailConfirm());
        assertEquals(Role.ADMIN, admin.getRole());
        assertFalse(admin.isDelete());
    }

    @Test
    @DisplayName("Oauth 멤버 생성")
    void createOauthMember() {
        // given

        String name = "이름";
        String email = "email@email.com";
        String password = "password";
        OAuthAttributes oAuthAttributes = OAuthAttributes.builder()
                .attributes(new HashMap<>())
                .name(name)
                .email(email)
                .socialType(SocialType.KAKAO)
                .password(password)
                .build();

        // when
        Member oauthMember = Member.createOauthMember(oAuthAttributes, oAuthAttributes.getName());

        // then
        assertEquals(name, oauthMember.getMemberName());
        assertEquals(email, oauthMember.getEmail());
        assertEquals(SocialType.KAKAO, oauthMember.getSocialType());
        assertEquals(password, oauthMember.getPassword());
        assertNotNull(oauthMember.getLessons());
        assertTrue(oauthMember.isEmailConfirm());
        assertEquals(Role.USER, oauthMember.getRole());
        assertFalse(oauthMember.isDelete());
    }

    @Test
    @DisplayName("Form 멤버 생성")
    void createFormMember() {
        // given
        String name = "이름";
        String email = "email@email.com";
        String password = "password";
        FormJoinDto formJoinDto = new FormJoinDto(name, email, password, password);

        // when
        Member formMember = Member.createFormMember(formJoinDto, formJoinDto.getPassword());

        // then
        assertEquals(name, formJoinDto.getMemberName());
        assertEquals(email, formJoinDto.getEmail());
        assertEquals(SocialType.FORM, formMember.getSocialType());
        assertEquals(password, formMember.getPassword());
        assertFalse(formMember.isEmailConfirm());
        assertNotNull(formMember.getLessons());
        assertEquals(Role.USER, formMember.getRole());
        assertFalse(formMember.isDelete());
    }

    @Test
    @DisplayName("Role key 조회")
    void getRoleKey() {
        // when
        String roleKey = testMember.getRoleKey();

        // then
        assertEquals(testMember.getRole().getKey(), roleKey);
    }

    @Test
    @DisplayName("멤버 이름 수정")
    void updateMemberName() {
        // given
        String newName = "newName";

        // when
        testMember.updateMemberName(newName);

        // then
        assertEquals(newName, testMember.getMemberName());
    }

    @Test
    @DisplayName("이메일 확인")
    void confirmEmail() {
        // given
        String confirmCode = "003212";
        testMember.updateConfirmEmailCode(confirmCode, LocalDateTime.of(2022, 1, 1, 12, 0));

        // when
        boolean result = testMember.confirmEmail(confirmCode);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("멤버 활성화")
    void activate() {
        // when
        testMember.activate();

        // then
        assertTrue(testMember.isEmailConfirm());
    }

    @Test
    @DisplayName("멤버 토큰 업데이트")
    void updateMemberToken() {
        // given
        LocalDateTime tokenExpireTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        MemberToken memberToken = new MemberToken(1L, "refresh", tokenExpireTime, testMember);

        // when
        testMember.updateMemberToken(memberToken);

        // then
        assertEquals(memberToken, testMember.getMemberToken());
    }

    @Test
    @DisplayName("이메일 확인 코드 업데이트")
    void updateConfirmEmailCode() {
        // given
        String emailConfirmCode = "001211";
        LocalDateTime emailConfirmCodeCreatedAt = LocalDateTime.of(2022, 1, 1, 12, 0);

        // when
        testMember.updateConfirmEmailCode(emailConfirmCode, emailConfirmCodeCreatedAt);

        // then
        assertEquals(emailConfirmCode, testMember.getEmailConfirmCode());
        assertEquals(emailConfirmCodeCreatedAt, testMember.getEmailConfirmCodeCreatedAt());
    }

    @Test
    @DisplayName("이메일 확인 코드 생성 가능 여부 확인 - 가능")
    void canCreateEmailConfirmCode() {
        // given
        String emailConfirmCode = "001211";
        LocalDateTime firstCreateAt = LocalDateTime.of(2022, 1, 1, 12, 0);
        testMember.updateConfirmEmailCode(emailConfirmCode, firstCreateAt);

        // when
        LocalDateTime secondCreatedAt = firstCreateAt.plusMinutes(5);
        boolean result = testMember.canCreateEmailConfirmCode(secondCreatedAt);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("이메일 확인 코드 생성 가능 여부 확인 - 불가능")
    void canCreateEmailConfirmCode_fail() {
        // given
        String emailConfirmCode = "001211";
        LocalDateTime firstCreateAt = LocalDateTime.of(2022, 1, 1, 12, 0);
        testMember.updateConfirmEmailCode(emailConfirmCode, firstCreateAt);

        // when
        LocalDateTime secondCreatedAt = firstCreateAt.plusMinutes(4);
        boolean result = testMember.canCreateEmailConfirmCode(secondCreatedAt);

        // then
        assertFalse(result);
    }
}