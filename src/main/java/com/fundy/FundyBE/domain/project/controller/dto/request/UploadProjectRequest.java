package com.fundy.FundyBE.domain.project.controller.dto.request;

import com.fundy.FundyBE.global.constraint.Day;
import com.fundy.FundyBE.global.constraint.GenreName;
import com.fundy.FundyBE.global.validation.annotation.enumlist.EnumList;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadProjectRequest {
    @Schema(description = "프로젝트명", example = "프로젝트: 리그오브펀디")
    @NotNull(message = "프로젝트명은 필수 입니다")
    @Size(min = 1, max = 30, message = "프로젝트명은 1~30자입니다")
    private String name;

    @Schema(description = "썸네일", example = "https://이미지주소")
    @NotNull(message = "썸네일은 필수입니다")
    @URL(message = "썸네일은 URL 형식이어야 합니다")
    private String thumbnail;

    @Schema(description = "서버 미디어(URL은 검토 안함)", example = "[image1, image2]")
    @Size(max = 9, message = "서브 미디어는 0~9장 까지입니다")
    private List<String> subMedias;

    @Schema(description = "장르들", example = "[액션, 슈팅]")
    @EnumList(enumClass = GenreName.class, message = "장르명이 올바르지 않습니다")
    @Size(min = 1, max = 5, message = "장르는 1~5개로 설정해야합니다")
    private List<String> genres;

    @Schema(description = "프로젝트 시작일", example = "")
    @NotNull(message = "프로젝트 시작일은 필수입니다")
    @Future(message = "시작일은 현재 이후로 설정되어야 됩니다")
    private LocalDateTime startDateTime;

    @Schema(description = "프로젝트 마감일", example = "")
    @NotNull(message = "프로젝트 마감일은 필수입니다")
    @Future(message = "마감일은 현재 이후로 설정되어야 됩니다")
    private LocalDateTime endDateTime;

    @Schema(description = "업로드 주차", example = "1")
    @NotNull(message = "업로드 주차는 필수입니다")
    @Min(value = 1, message = "업로드 주차는 최소 1주 이상이어야 합니다")
    @Max(value = 5, message = "업로드 주차는 최대 5주 이하이어야 합니다")
    private int devNoteUploadCycle;

    @Schema(description = "업로드 요일", example = "MONDAY")
    @NotNull(message = "업로드 요일은 필수입니다")
    private Day devNoteUploadDay;

    @Schema(description = "리워드")
    @Size(max = 7, message = "리워드는 0~7개 사이로 설정해야합니다")
    @Valid
    private List<ProjectRewardRequest> rewards;
}
