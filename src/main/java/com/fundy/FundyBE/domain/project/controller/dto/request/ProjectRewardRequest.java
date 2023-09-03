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

    @Schema(description = "리워드 설명", example = "해당 리워드는 ...")
    @NotNull(message = "메인이미지는 필수입니다")
    @Size(min = 2, max = 50, message = "리워드 설명은 2~50자로 제한되어 있습니다")
    private String description;

    @Schema(description = "리워드 최소 가격", example = "1000")
    @Min(value = 1, message = "최소 1원 이상이어야 합니다")
    @Max(value = 100000000, message = "최대 1억원까지 입니다")
    private int minimumPrice;
}
