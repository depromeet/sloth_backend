package com.sloth.creator;

import com.sloth.domain.nickname.Nickname;

public class TestNicknameCreator {

    public Nickname createNickname() {
        return Nickname.builder()
                .nicknameId(1L)
                .name("dragon")
                .isUsed(false)
                .build();
    }

}
