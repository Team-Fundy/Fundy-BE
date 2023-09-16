package com.fundy.FundyBE.domain.project.subdomain.reward.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RewardInfoServiceResponse {
    @Schema(description = "리워드 아이디", example = "1")
    private long id;
    @Schema(description = "리워드명", example = "리워드 1")
    private String name;
    @Schema(description = "최소 가격", example = "1000")
    private int minimumPrice;
    @Schema(description = "아이템들", example = "[아이템1, 아이템2]")
    private List<String> items;
    @Schema(description = "이미지", example = "http://이미주 주소")
    private String image;
}
