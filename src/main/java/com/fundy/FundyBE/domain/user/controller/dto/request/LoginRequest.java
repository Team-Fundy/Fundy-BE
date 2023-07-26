package com.fundy.FundyBE.domain.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Schema(description = "이메일", example = "dongwon0103@naver.com")
    @NotNull(message = "이메일은 필수 입니다")
    @Email(message = "이메일이 형식을 올바르지 않습니다")
    String email;
    // 대소문자, 숫자, 특수기호(@#$%^&+=!*)를 하나 이상 포함
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!*]).*$",message = "패턴이 옳지 않습니다")
    @NotNull(message = "패스워드가 없습니다")
    @Length(min = 10, max = 30)
    @Schema(description = "비밀번호", example = "!Ehddjfkladjflka12")
    String password;
}
