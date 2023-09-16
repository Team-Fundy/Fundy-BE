package com.fundy.FundyBE.domain.project.controller;

import com.fundy.FundyBE.domain.project.controller.dto.request.ProjectRewardRequest;
import com.fundy.FundyBE.domain.project.controller.dto.request.UploadProjectRequest;
import com.fundy.FundyBE.domain.project.service.ProjectService;
import com.fundy.FundyBE.domain.project.service.dto.request.ProjectRewardServiceRequest;
import com.fundy.FundyBE.domain.project.service.dto.request.UploadProjectServiceRequest;
import com.fundy.FundyBE.domain.project.subdomain.genre.service.GenreService;
import com.fundy.FundyBE.global.exception.response.ExceptionResponse;
import com.fundy.FundyBE.global.response.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "Project", description = "Project 도메인 관련 API 입니다.")
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final GenreService genreService;

    @Operation(summary = "프로젝트 업로드", description = "크리에이터 유저가 프로젝트 업로드",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공",
        useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "RequestBody가 조건에 맞지 않음",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "403", description = "인증 & 인가 문제",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 내부 에러",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public GlobalResponse<Long> uploadProject(
            @RequestPart @Valid UploadProjectRequest request,
            @RequestPart MultipartFile descriptionFile,
            @RequestParam(name = "promotion", defaultValue = "true") boolean isPromototion,
            @AuthenticationPrincipal User user) {
        UploadProjectServiceRequest uploadProjectServiceRequest = UploadProjectServiceRequest.builder()
                .name(request.getName())
                .thumbnail(request.getThumbnail())
                .subMedias(request.getSubMedias())
                .subDescription(request.getSubDescription())
                .genres(request.getGenres())
                .descriptionFile(descriptionFile)
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getEndDateTime())
                .devNoteUploadCycle(request.getDevNoteUploadCycle())
                .devNoteUploadDay(request.getDevNoteUploadDay())
                .isPromotion(isPromototion)
                .rewards(isNotNullConvertRewards(request.getRewards()))
                .userEmail(user.getUsername())
                .build();


        return GlobalResponse.<Long>builder()
                .message("업로드 성공")
                .result(projectService.uploadProject(uploadProjectServiceRequest))
                .build();
    }

    @Operation(summary = "장르 조회", description = "모든 게임 장르들을 조회")
    @ApiResponse(responseCode = "200", description = "성공",
            useReturnTypeSchema = true)
    @GetMapping("/genres")
    public GlobalResponse<List<String>> getAllGenres() {
        return GlobalResponse.<List<String>>builder()
                .message("장르 조회 성공")
                .result(genreService.getAllGenres())
                .build();
    }

    private List<ProjectRewardServiceRequest> isNotNullConvertRewards(List<ProjectRewardRequest> rewards) {
        if(rewards == null) {
            return null;
        }

        return rewards.stream().map((reward)->
                ProjectRewardServiceRequest.builder()
                        .name(reward.getName())
                        .items(reward.getItems())
                        .image(reward.getImage())
                        .minimumPrice(reward.getMinimumPrice())
                        .build())
                .collect(Collectors.toList());
    }
}
