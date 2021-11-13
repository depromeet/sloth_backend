package com.sloth.domain.nickname.service;

import com.sloth.domain.BaseService;
import com.sloth.domain.nickname.Nickname;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class NicknameServiceTest extends BaseService {

    @Autowired
    private NicknameService nicknameService;


    @Test
    @DisplayName("랜덤 닉네임 조회 테스트")
    public void findRandomNickname() {

        // when
        Nickname randomNickname = nicknameService.findRandomNickname();

        // then
        Assertions.assertThat(randomNickname).isNotNull();
    }

}