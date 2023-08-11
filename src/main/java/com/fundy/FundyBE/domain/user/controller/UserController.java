package com.fundy.FundyBE.domain.user.controller;

import com.fundy.FundyBE.domain.user.controller.dto.request.EmailCodeRequest;
import com.fundy.FundyBE.domain.user.controller.dto.request.LoginRequest;
import com.fundy.FundyBE.domain.user.controller.dto.request.SignUpRequest;
import com.fundy.FundyBE.domain.user.controller.dto.request.VerifyEmailRequest;
import com.fundy.FundyBE.domain.user.service.UserService;
import com.fundy.FundyBE.domain.user.service.dto.request.LoginServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.request.SignUpServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.request.VerifyEmailCodeServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.response.AvailableNicknameResponse;
import com.fundy.FundyBE.domain.user.service.dto.response.EmailCodeResponse;
import com.fundy.FundyBE.domain.user.service.dto.response.UserInfoResponse;
import com.fundy.FundyBE.domain.user.service.dto.response.VerifyEmailResponse;
import com.fundy.FundyBE.global.jwt.TokenInfo;
import com.fundy.FundyBE.global.response.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@Tag(name = "User", description = "User 도메인 관련 API 입니다.")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Operation(summary = "이메일 회원가입", description = "유저가 이메일로 회원가입 시도")
    @PostMapping("/sign-up")
    public GlobalResponse<UserInfoResponse> emailSignUp(@RequestBody @Valid final SignUpRequest signUpRequest) {
        UserInfoResponse result = userService.emailSignUp(SignUpServiceRequest.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .nickname(signUpRequest.getNickname())
                .profileImage(signUpRequest.getProfileImage())
                .build());

        return GlobalResponse.<UserInfoResponse>builder()
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

    @Operation(summary = "유저 정보 조회", description = "토큰으로 유저 정보 조회",
        security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/info")
    public GlobalResponse<UserInfoResponse> getUserInfo(Principal principal) {
        return GlobalResponse.<UserInfoResponse>builder()
                .message("User 정보 조회")
                .result(userService.findByEmail(principal.getName()))
                .build();
    }

    @Operation(summary = "이메일 인증 코드", description = "유저 이메일 인증 코드 6자리 발송")
    @PostMapping("/email/code")
    public GlobalResponse<EmailCodeResponse> sendEmailCode(@RequestBody @Valid final EmailCodeRequest emailCodeRequest) {
        return GlobalResponse.<EmailCodeResponse>builder()
                .message("인증코드 이메일 전송")
                .result(userService.sendEmailCodeAndReturnToken(emailCodeRequest.getEmail()))
                .build();
    }
    @Operation(summary = "이메일 인증", description = "유저 이메일 인증")
    @PostMapping("/email/verify")
    public GlobalResponse<VerifyEmailResponse> verifyEmail(@RequestBody @Valid final VerifyEmailRequest verifyEmailRequest) {
        return GlobalResponse.<VerifyEmailResponse>builder()
                .message("인증 여부 확인")
                .result(userService.verifyTokenWithEmail(
                        VerifyEmailCodeServiceRequest.builder()
                                .token(verifyEmailRequest.getToken())
                                .email(verifyEmailRequest.getEmail())
                                .code(verifyEmailRequest.getCode())
                                .build()))
                .build();
    }

    @Operation(summary = "닉네임 중복 검사", description = "유저 닉네임 중복 검사")
    @GetMapping("/check-nickname")
    public GlobalResponse<AvailableNicknameResponse> isAvailableNickname(
            @Parameter(description = "중복 검사할 닉네임", example = "유저-123")
            @RequestParam(name = "nickname") String nickname
    ) {
        return GlobalResponse.<AvailableNicknameResponse>builder()
                .message("닉네임 조회 성공")
                .result(userService.isAvailableNickname(nickname))
                .build();
    }
}
