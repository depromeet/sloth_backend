package com.sloth.api.login.oauth.social.service;

import com.sloth.global.config.auth.dto.OAuthAttributes;

public interface SocialApiSerivce {

    OAuthAttributes getUserInfo(String accessToken);

}
