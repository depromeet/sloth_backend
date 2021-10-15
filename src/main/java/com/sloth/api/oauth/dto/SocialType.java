package com.sloth.api.oauth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum SocialType {

    GOOGLE, KAKAO, APPLE;

    @JsonCreator
    public static SocialType from(String type) {
        return SocialType.valueOf(type.toUpperCase());
    }

    /**
     * 해당 문자열이 SocialType에 해당하는지 검사
     * @param type
     * @return
     */
    public static boolean isSocialType(String type) {
        List<SocialType> collect = Arrays.stream(SocialType.values())
                .filter(socialType -> socialType.name().equals(type))
                .collect(Collectors.toList());

        if(collect == null || collect.size() == 0) {
            return false;
        }

        return true;
    }

}