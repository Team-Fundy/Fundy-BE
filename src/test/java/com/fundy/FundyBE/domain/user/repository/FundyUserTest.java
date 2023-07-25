package com.fundy.FundyBE.domain.user.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FundyUserTest {
    String nickname = "nickname";
    String email = "email";
    String password = "password";
    String profileImage = "profileImage";

    @Test
    void getNickname() {
        // given
        FundyUser fundyUser = FundyUser.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .profileImage(profileImage)
                .build();

        // when, then
        Assertions.assertThat(fundyUser.getNickname()).isEqualTo(nickname);
        Assertions.assertThat(fundyUser.getEmail()).isEqualTo(email);
        Assertions.assertThat(fundyUser.getPassword()).isEqualTo(password);
        Assertions.assertThat(fundyUser.getProfileImage()).isEqualTo(profileImage);
    }
}