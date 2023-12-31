package com.fundy.FundyBE.domain.project.service.dto.request;

import com.fundy.FundyBE.global.constraint.Day;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class UploadProjectServiceRequest {
    private String name;
    private String thumbnail;
    private String subDescription;
    private List<String> subMedias;
    private List<String> genres;
    private MultipartFile descriptionFile;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int devNoteUploadCycle;
    private Day devNoteUploadDay;
    private List<ProjectRewardServiceRequest> rewards;
    private String userEmail;
    private boolean isPromotion;
}
