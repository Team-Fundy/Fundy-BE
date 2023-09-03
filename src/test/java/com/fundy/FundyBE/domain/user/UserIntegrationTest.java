package com.fundy.FundyBE.domain.user;

import com.fundy.FundyBE.BaseIntegrationTest;
import com.fundy.FundyBE.domain.user.controller.dto.request.LoginRequest;
import com.fundy.FundyBE.domain.user.controller.dto.request.SignUpRequest;
import com.fundy.FundyBE.global.constraint.AuthType;
import com.fundy.FundyBE.global.constraint.FundyRole;
import com.fundy.FundyBE.domain.user.repository.FundyUser;
import com.fundy.FundyBE.domain.user.repository.UserRepository;
import com.fundy.FundyBE.global.component.jwt.JwtProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 통합테스트")
public class UserIntegrationTest extends BaseIntegrationTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    PasswordEncoder passwordEncoder;
    String DEFAULT_PASSWORD = "@Fundy01";

    @DisplayName("[성공] 이메일 회원가입")
    @Test
    void emailSignUp() throws Exception {
        // given
        String email = "test01@naver.com";
        String password = "@FundyTest01";

        SignUpRequest request = SignUpRequest.builder()
                .email(email)
                .password(password)
                .build();


        // when
        ResultActions resultActions = mvc.perform(post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.email").value(email));
    }

    @DisplayName("[성공] 로그인")
    @Test
    void login() throws Exception{
        //given
        String password = "@FundyTest01";
        FundyUser defaultUser = getDefaultUser(password);
        LoginRequest request = LoginRequest.builder()
                .email(defaultUser.getEmail())
                .password(password)
                .build();
        saveUser(defaultUser);

        // when
        ResultActions resultActions = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.result.accessToken").exists());
        resultActions.andExpect(jsonPath("$.result.refreshToken").exists());
    }

    @DisplayName("[실패] 로그인: 패스워드 틀림")
    @Test
    void loginFail() throws Exception{
        //given
        String wrongPassword = "@FundyTest01";
        FundyUser defaultUser = getDefaultUser(DEFAULT_PASSWORD);
        LoginRequest request = LoginRequest.builder()
                .email(defaultUser.getEmail())
                .password(wrongPassword)
                .build();
        saveUser(defaultUser);

        // when
        ResultActions resultActions = mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isUnauthorized());
    }

    @DisplayName("[성공] 크리에이터 전환")
    @Test
    @WithMockUser(username = "test01@naver.com", authorities = "NORMAL_USER")
    void transformCreator() throws Exception{
        // given
        FundyUser defaultUser = getDefaultUser(DEFAULT_PASSWORD);
        saveUser(defaultUser);

        // when
        ResultActions resultActions = mvc.perform(put("/user/creator")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization",
                                "Bearer "+jwtProvider.generateToken(defaultUser.getEmail(), FundyRole.NORMAL_USER)
                                        .getAccessToken()))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.result.accessToken").exists());
        Assertions.assertThat(userRepository.findByEmail(defaultUser.getEmail()).get().getRole()).isEqualTo(FundyRole.CREATOR);
    }

    @DisplayName("[성공] 유저 정보 조회(JWT)")
    @Test
    @WithMockUser(username = "test01@naver.com", authorities = "NORMAL_USER")
    void userInfo() throws Exception{
        // given
        FundyUser defaultUser = getDefaultUser(DEFAULT_PASSWORD);
        saveUser(defaultUser);

        // when
        ResultActions resultActions = mvc.perform(get("/user/info")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(jsonPath("$.result.email").value(defaultUser.getEmail()));
    }

    @DisplayName("[성공] 닉네임 중복 검사: 중복 없음")
    @Test
    void checkNicknameNoDuplicate() throws Exception {
        // given
        FundyUser defaultUser = getDefaultUser(DEFAULT_PASSWORD);
        // when
        ResultActions resultActions = mvc.perform(get("/user/check-nickname")
                        .param("nickname", defaultUser.getNickname())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.result.available").value(true));
    }

    @DisplayName("[성공] 닉네임 중복 검사: 중복 존재")
    @Test
    void checkNicknameHasDuplicate() throws Exception {
        // given
        FundyUser defaultUser = getDefaultUser(DEFAULT_PASSWORD);
        saveUser(defaultUser);

        // when
        ResultActions resultActions = mvc.perform(get("/user/check-nickname")
                .param("nickname", defaultUser.getNickname())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.result.available").value(false));
    }

    private FundyUser getDefaultUser(String password) {
        return FundyUser.builder()
                .email("test01@naver.com")
                .password(passwordEncoder.encode(password))
                .profileImage("image")
                .role(FundyRole.NORMAL_USER)
                .authType(AuthType.EMAIL)
                .nickname("nickname")
                .build();
    }

    private FundyUser saveUser(FundyUser user) {
        return userRepository.save(user);
    }
}
