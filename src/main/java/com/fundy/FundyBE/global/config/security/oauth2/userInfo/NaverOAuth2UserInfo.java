package com.fundy.FundyBE.global.config.security.oauth2.userInfo;

import com.fundy.FundyBE.global.constraint.AuthType;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo{
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(AuthType.NAVER, (Map<String, Object>) attributes.get("response"));
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("profile_image");
    }
}
