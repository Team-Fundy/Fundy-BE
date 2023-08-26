package com.fundy.FundyBE.domain.user;

import com.fundy.FundyBE.domain.user.repository.AuthType;
import com.fundy.FundyBE.domain.user.repository.FundyRole;
import com.fundy.FundyBE.domain.user.repository.FundyUser;
import com.fundy.FundyBE.domain.user.repository.UserRepository;
import com.fundy.FundyBE.domain.user.service.UserService;
import com.fundy.FundyBE.domain.user.service.dto.request.SignUpServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.response.UserInfoResponse;
import com.fundy.FundyBE.global.exception.customException.DuplicateUserException;
import com.fundy.FundyBE.global.validation.user.UserValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("유저 서비스 유닛 테스트")
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock FundyUser mockUser;
    @Spy
    private PasswordEncoder passwordEncoder;

    @DisplayName("[성공] 회원가입: Default")
    @Test
    void emailSignUpSuccess() {
        // given
        String email = "test01@naver.com";
        String password = "$FundyTest";
        String nickname = "nickname";
        String profileImage = "profile_iamge";

        SignUpServiceRequest request = SignUpServiceRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .profileImage(profileImage)
                .build();

        given(userRepository.save(any(FundyUser.class))).willReturn(mockUser);
        given(mockUser.getId()).willReturn(UUID.randomUUID());
        given(mockUser.getEmail()).willReturn(email);
        given(mockUser.getNickname()).willReturn(nickname);
        given(mockUser.getRole()).willReturn(FundyRole.NORMAL_USER);
        given(mockUser.getAuthType()).willReturn(AuthType.EMAIL);
        given(mockUser.getProfileImage()).willReturn(profileImage);

        // when
        UserInfoResponse response = userService.emailSignUp(request);

        // then
        Assertions.assertThat(response.getEmail()).isEqualTo(email);
        verify(userRepository, times(1)).save(any(FundyUser.class));
    }

    @DisplayName("[실패] 회원가입: 중복 유저")
    @Test
    void emailSignUpFail() {
        // given
        String email = "test01@naver.com";
        String password = "$FundyTest";
        String nickname = "nickname";
        String profileImage = "profile_iamge";

        SignUpServiceRequest request = SignUpServiceRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .profileImage(profileImage)
                .build();

        willThrow(DuplicateUserException.createBasic()).given(userValidator).hasDuplicateNickname(nickname);


        // when, then
        Assertions.assertThatThrownBy(() -> userService.emailSignUp(request)).isInstanceOf(DuplicateUserException.class);
        verify(userRepository, times(0)).save(any(FundyUser.class));
    }
}
