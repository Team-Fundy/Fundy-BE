package com.fundy.FundyBE.domain.project;

import com.fundy.FundyBE.domain.project.repository.Project;
import com.fundy.FundyBE.domain.project.repository.ProjectRepository;
import com.fundy.FundyBE.domain.project.service.ProjectService;
import com.fundy.FundyBE.domain.project.service.dto.request.ProjectRewardServiceRequest;
import com.fundy.FundyBE.domain.project.service.dto.request.UploadProjectServiceRequest;
import com.fundy.FundyBE.domain.project.subdomain.genre.service.GenreService;
import com.fundy.FundyBE.domain.project.subdomain.reward.service.RewardService;
import com.fundy.FundyBE.domain.user.repository.FundyUser;
import com.fundy.FundyBE.domain.user.repository.UserRepository;
import com.fundy.FundyBE.global.constraint.Day;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("프로젝트 서비스 유닛 테스트")
public class ProjectServiceTest {
    @InjectMocks
    private ProjectService projectService;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GenreService genreService;
    @Mock
    private RewardService rewardService;
    @Mock
    private Project mockProject;
    @Mock
    private FundyUser mockUser;

    @DisplayName("[성공] 프로젝트 업로드: Default")
    @Test
    void uploadProjectSuccess() throws IOException {
        // given
        List<ProjectRewardServiceRequest> rewards = new ArrayList<>();
        rewards.add(ProjectRewardServiceRequest.builder()
                        .image("image")
                        .minimumPrice(2000)
                        .name("reward 1")
                        .items(Arrays.asList("i1"))
                .build());
        rewards.add(ProjectRewardServiceRequest.builder()
                .minimumPrice(3000)
                .name("reward 2")
                .items(Arrays.asList("i2"))
                .build());

        File file = new File("src/test/java/resources/test.html");

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endDateTime = startDateTime.plusDays(15);

        UploadProjectServiceRequest request = UploadProjectServiceRequest.builder()
                .name("project")
                .userEmail("test01@naver.com")
                .startDateTime(startDateTime)
                .thumbnail("http://이미지")
                .endDateTime(endDateTime) // 2주
                .subMedias(Collections.singletonList("image1"))
                .isPromotion(false)
                .genres(Arrays.asList("액션", "슈팅"))
                .descriptionFile(new MockMultipartFile("test.html", new FileInputStream(file)))
                .devNoteUploadCycle(2)
                .devNoteUploadDay(Day.MONDAY)
                .rewards(rewards)
                .build();

        given(projectRepository.save(any(Project.class))).willReturn(mockProject);
        given(mockProject.getId()).willReturn(1L);
        given(userRepository.findByEmail("test01@naver.com")).willReturn(Optional.of(mockUser));

        // when
        long projectId = projectService.uploadProject(request);

        // then
        Assertions.assertThat(projectId).isEqualTo(1);
        verify(projectRepository, times(1)).save(any(Project.class));
    }
}
