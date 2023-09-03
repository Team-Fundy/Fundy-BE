package com.fundy.FundyBE.domain.project.service;

import com.fundy.FundyBE.domain.project.repository.DevNoteUploadTerm;
import com.fundy.FundyBE.domain.project.repository.Project;
import com.fundy.FundyBE.domain.project.repository.ProjectPeriod;
import com.fundy.FundyBE.domain.project.repository.ProjectRepository;
import com.fundy.FundyBE.domain.project.service.dto.request.UploadProjectServiceRequest;
import com.fundy.FundyBE.domain.project.subdomain.genre.service.GenreService;
import com.fundy.FundyBE.domain.project.subdomain.genre.service.dto.request.SaveAllGenresServiceRequest;
import com.fundy.FundyBE.domain.project.subdomain.reward.service.RewardService;
import com.fundy.FundyBE.domain.project.subdomain.reward.service.dto.request.SaveAllRewardServiceRequest;
import com.fundy.FundyBE.domain.project.subdomain.reward.service.dto.request.SaveRewardServiceRequest;
import com.fundy.FundyBE.domain.user.repository.FundyUser;
import com.fundy.FundyBE.domain.user.repository.UserRepository;
import com.fundy.FundyBE.global.constraint.GenreName;
import com.fundy.FundyBE.global.exception.customexception.NoUserException;
import com.fundy.FundyBE.global.exception.customexception.ServerInternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final GenreService genreService;
    private final RewardService rewardService;

    @Transactional
    public long uploadProject(final UploadProjectServiceRequest request) {
        FundyUser owner = userRepository.findByEmail(request.getUserEmail()).orElseThrow(NoUserException::createBasic);
        Project project = projectRepository.save(Project.builder()
                .user(owner)
                .name(request.getName())
                .mainImages(request.getMainImages())
                .description(multipartFileToString(request.getDescriptionFile()))
                .devNoteUploadTerm(DevNoteUploadTerm.builder()
                        .weekCycle(request.getDevNoteUploadCycle())
                        .day(request.getDevNoteUploadDay())
                        .build())
                .projectPeriod(ProjectPeriod.of(
                        request.getStartDateTime(), request.getEndDateTime()))
                .build());

        genreService.saveAllGenres(SaveAllGenresServiceRequest.builder()
                .projectId(project.getId())
                .genreNames(request.getGenres().stream()
                        .map(GenreName::ofKorean)
                        .collect(Collectors.toList()))
                .build());

        rewardService.saveAllReward(SaveAllRewardServiceRequest.builder()
            .projectId(project.getId())
            .rewards(request.getRewards().stream().map((reward) ->
                    SaveRewardServiceRequest.builder()
                            .name(reward.getName())
                            .image(reward.getImage())
                            .minimumPrice(reward.getMinimumPrice())
                            .description(reward.getDescription())
                            .build())
                    .collect(Collectors.toList()))
            .build());

        return project.getId();
    }

    private String multipartFileToString(final MultipartFile multipartFile) {
        try {
            return new String(multipartFile.getBytes());
        } catch (IOException e) {
            log.error("Multifile to String convert problem");
            throw ServerInternalException.createBasic();
        }
    }
}
