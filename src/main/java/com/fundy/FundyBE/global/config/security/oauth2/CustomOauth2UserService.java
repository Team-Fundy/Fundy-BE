package com.fundy.FundyBE.global.config.security.oauth2;

import com.fundy.FundyBE.domain.user.repository.AuthType;
import com.fundy.FundyBE.domain.user.repository.FundyRole;
import com.fundy.FundyBE.domain.user.repository.FundyUser;
import com.fundy.FundyBE.domain.user.repository.UserRepository;
import com.fundy.FundyBE.global.config.security.oauth2.userInfo.OAuth2UserInfo;
import com.fundy.FundyBE.global.config.security.oauth2.userInfo.OAuth2UserInfoFactory;
import com.fundy.FundyBE.global.config.security.userDetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        return proccessOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User proccessOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        AuthType authType = AuthType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authType, oAuth2User.getAttributes());

        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new RuntimeException("OAuth2UserInfo not include email");
        }

        return CustomUserDetails.builder()
                .user(registerUserOrLogin(oAuth2UserInfo))
                .oAuth2UserInfo(oAuth2UserInfo)
                .build();
    }
    private FundyUser registerUserOrLogin(OAuth2UserInfo oAuth2UserInfo) {
        FundyUser fundyUser = userRepository.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);
        if(fundyUser == null) {
            return userRepository.save(FundyUser.builder()
                    .email(oAuth2UserInfo.getEmail())
                    .nickname(oAuth2UserInfo.getName())
                    .profileImage(oAuth2UserInfo.getImageUrl())
                    .role(FundyRole.GUEST)
                    .authType(oAuth2UserInfo.getAuthType())
                    .build());
        }

        if(fundyUser.getAuthType() != oAuth2UserInfo.getAuthType()) {
            throw new RuntimeException("User Already Signed Up, Different Provider");
        }

        return fundyUser;
    }
}
