package com.sloth.api.oauth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfo {
    private String email;
    private String name;
    private String picture;
}
