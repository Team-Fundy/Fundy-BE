package com.fundy.FundyBE.domain.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "이메일 인증 DTO")
public class VerifyEmailRequest {
    @NotNull(message = "이메일은 필수 입니다")
    @Email(message = "이메일이 올바르지 않습니다")
    @Schema(description = "이메일", example = "dongwon0103@naver.com")
    String email;

    @NotNull(message = "코드는 필수 입니다")
    @Length(min = 6, max = 6, message = "인증코드는 6자리 입니다")
    @Schema(description = "인증 코드", example = "050400")
    String code;

    @NotNull(message = "토큰은 필수 입니다")
    @Schema(description = "인증 토큰", example = "adfhjajdkfhajkdla")
    String token;
}
