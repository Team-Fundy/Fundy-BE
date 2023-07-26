package com.fundy.FundyBE.domain.user.controller;

import com.fundy.FundyBE.domain.user.controller.dto.request.LoginRequest;
import com.fundy.FundyBE.domain.user.controller.dto.request.SignUpRequest;
import com.fundy.FundyBE.domain.user.service.UserService;
import com.fundy.FundyBE.domain.user.service.dto.request.LoginServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.request.SignUpServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.response.SignUpServiceResponse;
import com.fundy.FundyBE.global.jwt.TokenInfo;
import com.fundy.FundyBE.global.response.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "User", description = "User 도메인 관련 API 입니다.")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Operation(summary = "이메일 회원가입", description = "유저가 이메일로 회원가입 시도")
    @PostMapping("/sign-up")
    public GlobalResponse<SignUpServiceResponse> emailSignUp(@RequestBody @Valid final SignUpRequest signUpRequest) {
        SignUpServiceResponse result = userService.emailSignUp(SignUpServiceRequest.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .nickname(signUpRequest.getNickname())
                .profileImage(signUpRequest.getProfileImage())
                .build());

        return GlobalResponse.<SignUpServiceResponse>builder()
                .message("User SignUp Successful")
                .result(result)
                .build();
    }

    @Operation(summary = "이메일로 로그인", description = "유저가 이메일로 로그인 시도")
    @PostMapping("/login")
    public GlobalResponse<TokenInfo> login(@RequestBody @Valid final LoginRequest loginRequest) {
        log.info("Call /user/login");
        TokenInfo tokenInfo = userService.login(LoginServiceRequest.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword()).build());
        return GlobalResponse.<TokenInfo>builder()
                .message("User login Successful")
                .result(tokenInfo)
                .build();
    }

    @Operation(summary = "인증 테스트", description = "유저 인증 테스트",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/test")
    public GlobalResponse<String> test() {
        return GlobalResponse.<String>builder()
                .message("인증 테스트 성공")
                .result("테스트 성공")
                .build();
    }
}
