package com.sloth.creator;

import com.sloth.domain.nickname.Nickname;

public class NicknameCreator {

    public static Nickname createStubNickname(String name) {
        return Nickname.builder()
                .nicknameId(1L)
                .name(name)
                .isUsed(false)
                .build();
    }

    public static Nickname createStubNickname(Long nicknameId, String name, boolean isUsed) {
        return Nickname.builder()
                .nicknameId(nicknameId)
                .name(name)
                .isUsed(isUsed)
                .build();
    }

}
