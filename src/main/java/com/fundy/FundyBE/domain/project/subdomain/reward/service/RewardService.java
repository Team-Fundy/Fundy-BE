package com.fundy.FundyBE.domain.project.subdomain.reward.service;

import com.fundy.FundyBE.domain.project.repository.Project;
import com.fundy.FundyBE.domain.project.repository.ProjectRepository;
import com.fundy.FundyBE.domain.project.subdomain.reward.repository.Reward;
import com.fundy.FundyBE.domain.project.subdomain.reward.repository.RewardRepository;
import com.fundy.FundyBE.domain.project.subdomain.reward.service.dto.request.SaveAllRewardServiceRequest;
import com.fundy.FundyBE.global.exception.customexception.NoProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardService {
    private final RewardRepository rewardRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void saveAllReward(SaveAllRewardServiceRequest request) {
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(NoProjectException::createBasic);

        rewardRepository.saveAll(request.getRewards().stream().map((reward)->
                        Reward.builder()
                                .name(reward.getName())
                                .image(reward.getImage())
                                .description(reward.getDescription())
                                .minimumPrice(reward.getMinimumPrice())
                                .project(project)
                                .build())
                .collect(Collectors.toList()));
    }
}
