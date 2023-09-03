package com.fundy.FundyBE.domain.project.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UploadProjectFormRequest {
    @Schema(description = "json")
    @NotNull(message = "request는 필수 입니다")
    @Valid
    private UploadProjectRequest request;

    @Schema(description = "설명 파일")
    @NotNull(message = "설명파일은 필수입니다")
    private MultipartFile descriptionFile;
}
