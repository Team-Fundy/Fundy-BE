package com.fundy.FundyBE.domain.user.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FundyUserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    String nickname = "nickname";
    String email = "email";
    String password = "password";
    String profileImage = "profileImage";

    @Test
    void save() {
        //given
        FundyUser fundyUser = FundyUser.builder()
                .email(email)
                .password(password)
                .profileImage(profileImage)
                .nickname(nickname)
                .build();

        // when
        FundyUser result = userRepository.save(fundyUser);

        // then
        Assertions.assertThat(fundyUser.getEmail()).isEqualTo(result.getEmail());
    }

    @Test
    void findByEmail() {
        //given
        FundyUser fundyUser = FundyUser.builder()
                .email(email)
                .password(password)
                .profileImage(profileImage)
                .nickname(nickname)
                .build();

        // when
        userRepository.save(fundyUser);
        Optional<FundyUser> result = userRepository.findByEmail(email);

        // then
        Assertions.assertThat(result.get().getEmail()).isEqualTo(fundyUser.getEmail());
    }

    @Test
    void findByNickname() {
        //given
        FundyUser fundyUser = FundyUser.builder()
                .email(email)
                .password(password)
                .profileImage(profileImage)
                .nickname(nickname)
                .build();

        // when
        userRepository.save(fundyUser);
        Optional<FundyUser> result = userRepository.findByNickname(nickname);

        // then
        Assertions.assertThat(result.get().getNickname()).isEqualTo(fundyUser.getNickname());
    }

    @Test
    void findById() {
        //given
        FundyUser fundyUser = FundyUser.builder()
                .email(email)
                .password(password)
                .profileImage(profileImage)
                .nickname(nickname)
                .build();

        FundyUser saveFundyUser = userRepository.save(fundyUser);

        // when
        Optional<FundyUser> result = userRepository.findById(UUID.fromString(saveFundyUser.getUsername()));

        // then
        Assertions.assertThat(result.get().getUsername()).isEqualTo(fundyUser.getUsername());
    }
}