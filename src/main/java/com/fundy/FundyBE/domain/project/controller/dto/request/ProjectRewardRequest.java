package com.fundy.FundyBE.domain.project.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectRewardRequest {
    @Schema(description = "리워드 이름", example = "리워드1")
    @NotNull(message = "리워드 명은 필수입니다")
    private String name;

    @Schema(description = "리워드 대표 사진", example = "http://이미지주소")
    @URL
    private String image;

    @Schema(description = "아이템들", example = "[아이템1,아이템2]")
    @Size(max = 10, message = "아이템들은 10개까지 설정 가능합니다")
    private List<String> items;

    @Schema(description = "리워드 최소 가격", example = "1000")
    @Min(value = 1, message = "최소 1원 이상이어야 합니다")
    @Max(value = 100000000, message = "최대 1억원까지 입니다")
    private int minimumPrice;
}
