package com.fundy.FundyBE.domain.project.service.dto.response;

import com.fundy.FundyBE.global.constraint.Day;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectInfoServiceResponse {
    @Schema(description = "프로젝트 아이디", example = "1")
    private long id;
    @Schema(description = "프로젝트명", example = "FundyProject")
    private String name;
    @Schema(description = "썸네일", example = "http://썸네일주소")
    private String thumbnail;
    @Schema(description = "홍보 프로젝트인지", example = "false")
    private boolean isPromotion;
    @Schema(description = "서브 이미지/영상", example = "[이미지,영상..]")
    private List<String> subMedias;
    @Schema(description = "프로젝트 시작일(시간포함)", example = "시작일")
    private LocalDateTime startDate;
    @Schema(description = "프로젝트 출시일(시간포함)", example = "출시일")
    private LocalDateTime endDate;
    @Schema(description = "프로젝트 업로드 주기(주)", example = "3")
    private int devNoteUploadCycle;
    @Schema(description = "프로젝트 업로드 요일", example = "FRIDAY")
    private Day devNoteUploadDay;
    @Schema(description = "장르들", example = "[액션,슈팅]")
    private List<String> genres;
    @Schema(description = "설명 파일 Text", example = "파일 내용")
    private String description;
    @Schema(description = "간단 설명", example = "프로젝트 간단 설명")
    private String subDescription;
    @Schema(description = "프로젝트 생성일", example = "생성일")
    private LocalDateTime createdAt;
    @Schema(description = "프로젝트 변경일", example = "변경일")
    private LocalDateTime modifiedAt;
}
