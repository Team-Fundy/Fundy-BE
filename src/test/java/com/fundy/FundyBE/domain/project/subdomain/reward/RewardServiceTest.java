package com.fundy.FundyBE.domain.project.subdomain.reward;

import com.fundy.FundyBE.domain.project.repository.Project;
import com.fundy.FundyBE.domain.project.subdomain.reward.repository.Reward;
import com.fundy.FundyBE.domain.project.subdomain.reward.repository.RewardRepository;
import com.fundy.FundyBE.domain.project.subdomain.reward.service.RewardService;
import com.fundy.FundyBE.domain.project.subdomain.reward.service.dto.response.RewardInfoServiceResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("리워드 서비스 유닛 테스트")
public class RewardServiceTest {
    @InjectMocks
    private RewardService rewardService;
    @Mock
    private RewardRepository rewardRepository;
    @Mock
    private Project mockProject;

    @DisplayName("[성공] 리워드 조회: Default")
    @Test
    void findByProjectId() {
        // given
        long projectId = 1;

        Reward reward1 = mock(Reward.class);
        Reward reward2 = mock(Reward.class);
        given(rewardRepository.findByProjectId(projectId)).willReturn(
                Arrays.asList(reward1,reward2));

        given(reward1.getId()).willReturn(1L);
        given(reward2.getId()).willReturn(2L);

        // when
        List<RewardInfoServiceResponse> response = rewardService.findByProjectId(projectId);

        // then
        Assertions.assertThat(response.size()).isEqualTo(2);
        verify(rewardRepository, times(1)).findByProjectId(projectId);
    }
}
