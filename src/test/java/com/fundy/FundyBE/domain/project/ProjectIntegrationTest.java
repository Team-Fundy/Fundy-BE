package com.fundy.FundyBE.domain.project;

import com.fundy.FundyBE.BaseIntegrationTest;
import com.fundy.FundyBE.domain.project.controller.dto.request.ProjectRewardRequest;
import com.fundy.FundyBE.domain.project.controller.dto.request.UploadProjectRequest;
import com.fundy.FundyBE.domain.project.repository.Project;
import com.fundy.FundyBE.domain.project.repository.ProjectRepository;
import com.fundy.FundyBE.domain.project.subdomain.genre.repository.Genre;
import com.fundy.FundyBE.domain.project.subdomain.genre.repository.GenreRepository;
import com.fundy.FundyBE.domain.project.subdomain.reward.repository.Reward;
import com.fundy.FundyBE.domain.project.subdomain.reward.repository.RewardRepository;
import com.fundy.FundyBE.domain.user.repository.FundyUser;
import com.fundy.FundyBE.domain.user.repository.UserRepository;
import com.fundy.FundyBE.global.constraint.AuthType;
import com.fundy.FundyBE.global.constraint.Day;
import com.fundy.FundyBE.global.constraint.FundyRole;
import com.fundy.FundyBE.global.constraint.GenreName;
import com.fundy.FundyBE.global.exception.customexception.NoProjectException;
import com.fundy.FundyBE.global.response.GlobalResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 통합테스트")
public class ProjectIntegrationTest extends BaseIntegrationTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    RewardRepository rewardRepository;

    @DisplayName("[성공] 장르들 조회")
    @Test
    void getAllGenres() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/project/genres")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result", hasSize(GenreName.values().length)));
    }

    @DisplayName("[성공] 홍보용 프로젝트 업로드")
    @Test
    @WithMockUser(username = "creator01@naver.com", authorities = "CREATOR")
    void uploadPromotionProject() throws Exception {
        // given
        FundyUser creator = saveUser(getDefaultCreator());
        File file = new File("src/test/java/resources/test.html");
        MockMultipartFile descriptionFile = new MockMultipartFile(
                "descriptionFile", new FileInputStream(file));

        LocalDateTime now = LocalDateTime.now();

        UploadProjectRequest request = UploadProjectRequest.builder()
                .name("name")
                .subDescription("간단 설명")
                .genres(Arrays.asList("액션", "슈팅"))
                .thumbnail("http://이미지썸네일")
                .subMedias(Arrays.asList("http://이미지1", "http://이미지2"))
                .startDateTime(now.plusDays(1))
                .endDateTime(now.plusDays(20))
                .devNoteUploadCycle(2)
                .devNoteUploadDay(Day.MONDAY)
                .build();

        MockMultipartFile requestFile = new MockMultipartFile(
                "request", "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request));

        // when
        ResultActions resultActions = mvc.perform(multipart("/project")
                        .file(descriptionFile)
                        .file(requestFile)
                        .queryParam("promotion", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.result").exists());

        MockHttpServletResponse mvcResponse = resultActions.andReturn().getResponse();
        mvcResponse.setCharacterEncoding("UTF-8");
        String content = mvcResponse.getContentAsString();

        GlobalResponse<Long> response = objectMapper.readValue(content, GlobalResponse.class);
        Number result = response.getResult();
        Long projectId = result.longValue();

        Project project = projectRepository.findById(projectId).orElseThrow(NoProjectException::createBasic);
        Assertions.assertThat(project.getUser()).isEqualTo(creator);
    }

    @DisplayName("[성공] 프로젝트 업로드")
    @Test
    @WithMockUser(username = "creator01@naver.com", authorities = "CREATOR")
    void uploadProject() throws Exception {
        // given
        FundyUser creator = saveUser(getDefaultCreator());

        File file = new File("src/test/java/resources/test.html");
        MockMultipartFile descriptionFile = new MockMultipartFile(
                "descriptionFile", new FileInputStream(file));

        LocalDateTime now = LocalDateTime.now();
        List<ProjectRewardRequest> projectRewardRequest = Arrays.asList(
                ProjectRewardRequest.builder()
                        .name("reward 1")
                        .image("http://리워드-이미지")
                        .items(Arrays.asList("아이템1","아이템2"))
                        .minimumPrice(1000)
                        .build(),
                ProjectRewardRequest.builder()
                        .name("reward 2")
                        .items(Arrays.asList("아이템1","아이템2","아이템3"))
                        .minimumPrice(2000)
                        .build());

        UploadProjectRequest request = UploadProjectRequest.builder()
                .name("name")
                .genres(Arrays.asList("액션", "슈팅"))
                .subDescription("간단 설명")
                .thumbnail("http://이미지썸네일")
                .subMedias(Arrays.asList("http://이미지1", "http://이미지2"))
                .startDateTime(now.plusDays(1))
                .endDateTime(now.plusDays(20))
                .devNoteUploadCycle(2)
                .devNoteUploadDay(Day.MONDAY)
                .rewards(projectRewardRequest)
                .build();

        MockMultipartFile requestFile = new MockMultipartFile(
                "request", "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request));

        // when
        ResultActions resultActions = mvc.perform(multipart("/project")
                        .file(descriptionFile)
                        .file(requestFile)
                        .queryParam("promotion", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.result").exists());

        MockHttpServletResponse mvcResponse = resultActions.andReturn().getResponse();
        mvcResponse.setCharacterEncoding("UTF-8");
        String content = mvcResponse.getContentAsString();

        GlobalResponse<Long> response = objectMapper.readValue(content, GlobalResponse.class);
        Number result = response.getResult();
        Long projectId = result.longValue();


        Project project = projectRepository.findById(projectId).orElseThrow(NoProjectException::createBasic);
        Assertions.assertThat(project.getUser()).isEqualTo(creator);

        List<Genre> genres = genreRepository.findByProjectId(projectId);
        Assertions.assertThat(genres.size()).isEqualTo(2);

        List<Reward> rewards = rewardRepository.findByProjectId(projectId);
        Assertions.assertThat(rewards.size()).isEqualTo(2);
    }

    @DisplayName("[실패] 프로젝트 업로드: 조건 검사")
    @Test
    @WithMockUser(username = "creator01@naver.com", authorities = "CREATOR")
    void uploadProjectFailCase1() throws Exception {
        // given
        FundyUser creator = saveUser(getDefaultCreator());

        File file = new File("src/test/java/resources/test.html");
        MockMultipartFile descriptionFile = new MockMultipartFile(
                "descriptionFile", new FileInputStream(file));

        List<ProjectRewardRequest> rewards = Arrays.asList(
                ProjectRewardRequest.builder()
                        .name("reward 1")
                        .image("이미지") // 잘못된 케이스
                        .minimumPrice(1000)
                        .build(),
                ProjectRewardRequest.builder()
                        .name("reward 2")
                        .items(Arrays.asList("아이템1"))
                        .minimumPrice(0) // 잘못된 케이스
                        .build());

        LocalDateTime now = LocalDateTime.now();
        UploadProjectRequest request = UploadProjectRequest.builder()
                .name("name")
                .thumbnail("썸네일") //잘못된 케이스
                .subDescription("간단 설명")
                .subMedias(new ArrayList<>())
                .genres(Arrays.asList("액", "슈")) // 잘못된 케이스
                .startDateTime(now.minusDays(1)) // 잘못된 케이스
                .endDateTime(now.plusDays(20))
                .devNoteUploadCycle(5)
                .devNoteUploadDay(Day.MONDAY)
                .rewards(rewards) // 잘못된 케이스
                .build();

        MockMultipartFile requestFile = new MockMultipartFile(
                "request", "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request));

        // when
        ResultActions resultActions = mvc.perform(multipart("/project")
                        .file(descriptionFile)
                        .file(requestFile)
                        .queryParam("promotion", "false")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.message", hasSize(5)));
    }


    private FundyUser getDefaultCreator() {
        return FundyUser.builder()
                .email("creator01@naver.com")
                .password("@Test023fd")
                .profileImage("image")
                .role(FundyRole.CREATOR)
                .authType(AuthType.EMAIL)
                .nickname("creator")
                .build();
    }

    private FundyUser saveUser(FundyUser user) {
        return userRepository.save(user);
    }
}
