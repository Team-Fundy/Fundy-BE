package com.fundy.FundyBE.domain.user.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VerifyEmailCodeServiceRequest {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String code;
    @NotNull
    private String token;
}
