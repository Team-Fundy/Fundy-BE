package com.fundy.FundyBE.domain.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailCodeRequest {
    @Schema(description = "이메일", example = "dongwon0103@naver.com")
    @NotNull(message = "이메일은 필수 입니다")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    String email;
}
