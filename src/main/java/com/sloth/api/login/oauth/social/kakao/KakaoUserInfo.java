package com.sloth.api.login.oauth.social.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties({"properties", "connected_at"})
public class KakaoUserInfo {
    @JsonProperty("id")
    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter @Setter
    @JsonIgnoreProperties({"profile_image_needs_agreement","profile_needs_agreement","profile_nickname_needs_agreement","gender_needs_agreement","has_gender","is_email_verified", "has_email", "email_needs_agreement", "is_email_valid"})
    public static class KakaoAccount {

        @JsonProperty("email")
        private String email;

        @JsonProperty("gender")
        private String gender;

        @JsonProperty("profile")
        private Profile profile;

        @Getter @Setter
        public static class Profile {
            @JsonProperty("nickname")
            private String nickname;

            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;

            @JsonProperty("is_default_image")
            private Boolean isDefaultImage;
        }

    }
}
