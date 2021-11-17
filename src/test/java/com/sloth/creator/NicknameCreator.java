package com.sloth.creator;

import com.sloth.domain.nickname.Nickname;

public class NicknameCreator {

    public Nickname createStubNickname(String name) {
        return Nickname.builder()
                .nicknameId(1L)
                .name(name)
                .isUsed(false)
                .build();
    }

}
