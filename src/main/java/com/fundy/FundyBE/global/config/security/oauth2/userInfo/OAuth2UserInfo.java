package com.fundy.FundyBE.global.config.security.oauth2.userInfo;

import com.fundy.FundyBE.domain.user.repository.AuthType;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;
    private final AuthType authType;

    public OAuth2UserInfo(AuthType authType, Map<String, Object> attributes) {
        this.authType = authType;
        this.attributes = attributes;
    }

    public AuthType getAuthType() {
        return authType;
    }
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
    public abstract String getImageUrl();
}
