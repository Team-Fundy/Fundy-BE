package com.fundy.FundyBE.global.config.security.oauth2.userInfo;

import com.fundy.FundyBE.global.constraint.AuthType;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(AuthType authType, Map<String, Object> attributes) {
        switch (authType) {
            case GOOGLE:
                return new GoogleOAuth2UserInfo(attributes);
            case KAKAO:
                return new KakaoOAuth2UserInfo(attributes);
            case NAVER:
                return new NaverOAuth2UserInfo(attributes);
            default:
                throw new IllegalArgumentException("존재하지 않는 SocialType");
        }
    }
}
