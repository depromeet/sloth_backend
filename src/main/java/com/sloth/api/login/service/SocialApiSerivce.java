package com.sloth.api.login.service;

import com.sloth.config.auth.dto.OAuthAttributes;

public interface SocialApiSerivce {

    OAuthAttributes getUserInfo(String accessToken);

}
