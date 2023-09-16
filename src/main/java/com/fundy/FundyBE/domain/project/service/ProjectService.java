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
import com.fundy.FundyBE.global.exception.customexception.NoProjectException;
import com.fundy.FundyBE.global.exception.customexception.NoUserException;
import com.fundy.FundyBE.global.exception.customexception.ServerInternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
                .thumbnail(request.getThumbnail())
                .subMedias(request.getSubMedias())
                .subDescription(request.getSubDescription())
                .isPromotion(request.isPromotion())
                .description(multipartFileToString(request.getDescriptionFile()))
                .devNoteUploadTerm(DevNoteUploadTerm.builder()
                        .weekCycle(request.getDevNoteUploadCycle())
                        .day(request.getDevNoteUploadDay())
                        .build())
                .projectPeriod(ProjectPeriod.of(
                        request.getStartDateTime(), request.getEndDateTime()))
                .build());

        System.out.println(request.isPromotion());
        if(request.isPromotion()) {
            System.out.println("is promotion");
            return project.getId();
        }

        genreService.saveAllGenres(SaveAllGenresServiceRequest.builder()
                .projectId(project.getId())
                .genreNames(removeDuplicate(request.getGenres()).stream()
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
                            .items(reward.getItems())
                            .build())
                    .collect(Collectors.toList()))
            .build());

        return project.getId();
    }

    private void findById(long id) {
        Project project = projectRepository.findById(id).orElseThrow(NoProjectException::createBasic);

    }

    private List<String> removeDuplicate(List<String> target) {
        Set<String> set = new HashSet<>(target);
        return new ArrayList<String>(set);
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
