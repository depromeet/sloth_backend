package com.sloth.domain.nickname;

import com.sloth.creator.NicknameCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NicknameTest {

    @Test
    @DisplayName("닉네임 사용여부 업데이트 테스트")
    public void updateUsedTest() {

        // given
        Nickname nickname = NicknameCreator.createStubNickname(1L, "dragon", false);

        // when
        nickname.updateUsed();

        // then
        Assertions.assertThat(nickname.isUsed()).isEqualTo(true);

    }


}