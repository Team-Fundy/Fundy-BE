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
@Schema(description = "유저 정보 Response")
public class UserInfoResponse {
    @Schema(description = "DB에 저장된 ID", example = "3bdfjakldfjalkdfj2-df...")
    private String id;
    @Schema(description = "이메일", example = "dongwon0103@naver.com")
    private String email;
    @Schema(description = "닉네임", example = "동원 12")
    private String nickname;
    @Schema(description = "프로필 이미 URL", example = "http://이미지 주소")
    private String profileImage;
}
