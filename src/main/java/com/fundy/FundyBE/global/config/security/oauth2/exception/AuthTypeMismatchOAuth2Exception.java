package com.fundy.FundyBE.global.config.security.oauth2.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class AuthTypeMismatchOAuth2Exception extends OAuth2AuthenticationException {
    private AuthTypeMismatchOAuth2Exception(String message) {
        super(message);
    }

    public static AuthTypeMismatchOAuth2Exception createBasic() {
        return new AuthTypeMismatchOAuth2Exception("AuthType Mismatch");
    }
}
