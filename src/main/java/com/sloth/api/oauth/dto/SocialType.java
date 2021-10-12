package com.sloth.api.oauth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum SocialType {
    GOOGLE, KAKAO, APPLE;

    @JsonCreator
    public static SocialType from(String type) {
        return SocialType.valueOf(type.toUpperCase());
    }

}