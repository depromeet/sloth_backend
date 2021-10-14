package com.sloth.api.oauth.service;

import com.sloth.config.auth.dto.OAuthAttributes;

public interface SocialApiSerivce {

    OAuthAttributes getUserInfo(String accessToken);

}
