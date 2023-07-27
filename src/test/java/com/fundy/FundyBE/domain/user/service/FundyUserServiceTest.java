package com.fundy.FundyBE.domain.user.service;

import com.fundy.FundyBE.domain.user.repository.FundyUser;
import com.fundy.FundyBE.domain.user.repository.UserRepository;
import com.fundy.FundyBE.domain.user.service.dto.request.SignUpServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.response.UserInfoServiceResponse;
import com.fundy.FundyBE.global.validation.user.UserValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class FundyUserServiceTest {
    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    UserValidator userValidator;

    @Mock
    FundyUser mockFundyUser;

    @Spy
    PasswordEncoder passwordEncoder;

    String email = "dongwon000@naver.com";
    String password = "phadf@naver1";

    @Test
    void emailSignUp() {
        //given
        doReturn(mockFundyUser).when(userRepository).save(any(FundyUser.class));
        doNothing().when(userValidator).hasDuplicateNickname(any());
        doNothing().when(userValidator).hasDuplicateEmail(any());
        doReturn(email).when(mockFundyUser).getEmail();
        doReturn("id").when(mockFundyUser).getUsername();

        SignUpServiceRequest request = SignUpServiceRequest.builder()
                .email(email)
                .password(password)
                .build();

        //when
        UserInfoServiceResponse response = userService.emailSignUp(request);

        //then
        Assertions.assertThat(response.getEmail()).isEqualTo(request.getEmail());
    }
}