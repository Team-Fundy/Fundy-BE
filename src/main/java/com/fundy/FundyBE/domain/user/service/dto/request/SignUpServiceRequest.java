package com.fundy.FundyBE.domain.user.service.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SignUpServiceRequest {
    private String email;
    private String nickname;
    private String password;
    private String profileImage;
}
