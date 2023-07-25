package com.fundy.FundyBE.domain.user.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SignUpServiceRequest {
    @NotNull
    @Email
    private String email;
    @Length(min = 2, max = 30)
    private String nickname;
    // 대소문자, 숫자, 특수기호(@#$%^&+=!*)를 하나 이상 포함
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!*]).*$")
    @NotNull
    @Length(min = 10, max = 30)
    private String password;
    @URL
    private String profileImage;
}
