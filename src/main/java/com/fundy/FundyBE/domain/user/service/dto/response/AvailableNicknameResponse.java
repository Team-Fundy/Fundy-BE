package com.fundy.FundyBE.domain.user.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Schema(name = "닉네임 중복 검사", description = "닉네임 중복 검사 DTO")
public class AvailableNicknameResponse {
    @Schema(name = "닉네임", description = "검사 요청한 닉네임", example = "닉네임")
    private String nickname;
    @Schema(name = "사용가능여부", description = "사용 가능하면 true", example = "true")
    private boolean available;
}
