package com.fundy.FundyBE.domain.user.controller;

import com.fundy.FundyBE.domain.user.controller.dto.request.EmailCodeRequest;
import com.fundy.FundyBE.domain.user.controller.dto.request.LoginRequest;
import com.fundy.FundyBE.domain.user.controller.dto.request.SignUpRequest;
import com.fundy.FundyBE.domain.user.controller.dto.request.VerifyEmailRequest;
import com.fundy.FundyBE.domain.user.service.EmailVerificationService;
import com.fundy.FundyBE.domain.user.service.UserService;
import com.fundy.FundyBE.domain.user.service.dto.request.LoginServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.request.SignUpServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.request.VerifyEmailCodeServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.response.AvailableNicknameResponse;
import com.fundy.FundyBE.domain.user.service.dto.response.EmailCodeResponse;
import com.fundy.FundyBE.domain.user.service.dto.response.TokenInfoResponse;
import com.fundy.FundyBE.domain.user.service.dto.response.UserInfoResponse;
import com.fundy.FundyBE.domain.user.service.dto.response.VerifyEmailResponse;
import com.fundy.FundyBE.global.component.jwt.dto.response.TokenInfo;
import com.fundy.FundyBE.global.component.s3.S3Uploader;
import com.fundy.FundyBE.global.exception.response.ExceptionResponse;
import com.fundy.FundyBE.global.exception.response.JwtExceptionResponse;
import com.fundy.FundyBE.global.response.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = "User", description = "User 도메인 관련 API 입니다.")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final S3Uploader s3Uploader;
    @Operation(summary = "이메일 회원가입", description = "유저가 이메일로 회원가입 시도")
    @ApiResponse(responseCode = "200", description = "성공",
            useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
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
    @ApiResponse(responseCode = "200", description = "성공",
            useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "401", description = "로그인 에러",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PostMapping("/login")
    public GlobalResponse<TokenInfoResponse> login(@RequestBody @Valid final LoginRequest loginRequest) {
        TokenInfoResponse tokenInfoResponse = userService.login(LoginServiceRequest.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword()).build());
        return GlobalResponse.<TokenInfoResponse>builder()
                .message("User login Successful")
                .result(tokenInfoResponse)
                .build();
    }

    @Operation(summary = "유저 정보 조회", description = "토큰으로 유저 정보 조회",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공",
            useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "토큰 이상",
            content = @Content(schema = @Schema(implementation = JwtExceptionResponse.class)))
    @GetMapping("/info")
    public GlobalResponse<UserInfoResponse> getUserInfo(@AuthenticationPrincipal User user) {
        return GlobalResponse.<UserInfoResponse>builder()
                .message("User 정보 조회")
                .result(userService.findByEmail(user.getUsername()))
                .build();
    }

    @Operation(summary = "이메일 인증 코드", description = "유저 이메일 인증 코드 6자리 발송")
    @ApiResponse(responseCode = "200", description = "성공",
            useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PostMapping("/email/code")
    public GlobalResponse<EmailCodeResponse> sendEmailCode(@RequestBody @Valid final EmailCodeRequest emailCodeRequest) {
        return GlobalResponse.<EmailCodeResponse>builder()
                .message("인증코드 이메일 전송")
                .result(emailVerificationService.sendEmailCodeAndReturnToken(emailCodeRequest.getEmail()))
                .build();
    }
    @Operation(summary = "이메일 인증", description = "유저 이메일 인증")
    @ApiResponse(responseCode = "200", description = "성공",
            useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PostMapping("/email/verify")
    public GlobalResponse<VerifyEmailResponse> verifyEmail(@RequestBody @Valid final VerifyEmailRequest verifyEmailRequest) {
        return GlobalResponse.<VerifyEmailResponse>builder()
                .message("인증 여부 확인")
                .result(emailVerificationService.verifyTokenWithEmail(
                        VerifyEmailCodeServiceRequest.builder()
                                .token(verifyEmailRequest.getToken())
                                .email(verifyEmailRequest.getEmail())
                                .code(verifyEmailRequest.getCode())
                                .build()))
                .build();
    }

    @Operation(summary = "닉네임 중복 검사", description = "유저 닉네임 중복 검사")
    @ApiResponse(responseCode = "200", description = "성공",
            useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
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
    @Operation(summary = "토큰 재발급", description = "토큰 재발급",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공",
            useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "토큰 이상",
            content = @Content(schema = @Schema(implementation = JwtExceptionResponse.class)))
    @GetMapping("/reissue")
    GlobalResponse<TokenInfoResponse> reissueToken(HttpServletRequest request) {
        return GlobalResponse.<TokenInfoResponse>builder()
                .message("재발급 성공")
                .result(userService.reissueToken(request))
                .build();
    }

    @Operation(summary = "로그아웃", description = "로그아웃",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공",
            useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "토큰 이상",
            content = @Content(schema = @Schema(implementation = JwtExceptionResponse.class)))
    @PostMapping("/logout")
    GlobalResponse<Boolean> logout(HttpServletRequest request) {
        userService.logout(request);
        return GlobalResponse.<Boolean>builder()
                .message("로그아웃 성공")
                .result(true)
                .build();
    }

    @Operation(summary = "유저 프로필 이미지 업로드", description = "유저 프로필 이미지 업로드")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생",
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "413", description = "파일 크기 에러",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    GlobalResponse<String> uploadProfileImage(@RequestParam("file")MultipartFile multipartFile) {
        return GlobalResponse.<String>builder()
                .message("이미지 업로드 성공")
                .result(s3Uploader.uploadProfileImage(multipartFile))
                .build();
    }
    @Operation(summary = "크레이터 전환", description = "크레에이터 전환",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @PutMapping(value = "/creator")
    GlobalResponse<TokenInfo> transformCreator(@AuthenticationPrincipal User user, HttpServletRequest request) {
        return GlobalResponse.<TokenInfo>builder()
                .message("크리에이터 전환 성공")
                .result(userService.transformCreator(request, user.getUsername()))
                .build();
    }
}
