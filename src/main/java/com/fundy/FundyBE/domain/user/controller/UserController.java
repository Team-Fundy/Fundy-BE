package com.fundy.FundyBE.domain.user.controller;

import com.fundy.FundyBE.domain.user.controller.dto.request.LoginRequest;
import com.fundy.FundyBE.domain.user.controller.dto.request.SignUpRequest;
import com.fundy.FundyBE.domain.user.service.UserService;
import com.fundy.FundyBE.domain.user.service.dto.request.LoginServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.request.SignUpServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.response.SignUpServiceResponse;
import com.fundy.FundyBE.global.jwt.TokenInfo;
import com.fundy.FundyBE.global.response.GlobalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public GlobalResponse emailSignUp(@RequestBody @Valid final SignUpRequest signUpRequest) {
        SignUpServiceResponse result = userService.emailSignUp(SignUpServiceRequest.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .nickname(signUpRequest.getNickname())
                .profileImage(signUpRequest.getProfileImage())
                .build());

        return GlobalResponse.<SignUpServiceResponse>builder()
                .success(true)
                .message("User SignUp Successful")
                .result(result)
                .build();
    }

    @PostMapping("/login")
    public GlobalResponse login(@RequestBody @Valid final LoginRequest loginRequest) {
        log.info("Call /user/login");
        TokenInfo tokenInfo = userService.login(LoginServiceRequest.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword()).build());
        return GlobalResponse.<TokenInfo>builder()
                .success(true)
                .message("User login Successful")
                .result(tokenInfo)
                .build();
    }

    @GetMapping("/test")
    public String test() {
        return "GlobalResponse.<String>";
    }
}
