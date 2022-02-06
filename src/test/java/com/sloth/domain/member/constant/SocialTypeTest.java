package com.sloth.domain.member.constant;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class SocialTypeTest {

    @Test
    @DisplayName("Social 타입으로 반환")
    void from() {
        // when
        SocialType socialType = SocialType.from("kAKao");

        // then
        assertEquals(SocialType.KAKAO, socialType);
    }

    @Test
    @DisplayName("Social 타입에 존재하는 지 확인 - 존재")
    void isSocialType() {
        // given
        String socialType = "KAKAO";

        // when
        boolean result = SocialType.isSocialType(socialType);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("Social 타입에 존재하는 지 확인 - 존재 안함")
    void isSocialType_fail() {
        // given
        String abc = "abc";

        // when
        boolean result = SocialType.isSocialType(abc);

        // then
        assertFalse(result);
    }
}