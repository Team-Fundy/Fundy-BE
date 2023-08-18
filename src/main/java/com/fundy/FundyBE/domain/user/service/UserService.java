package com.fundy.FundyBE.domain.user.service;

import com.fundy.FundyBE.domain.user.repository.AuthType;
import com.fundy.FundyBE.domain.user.repository.FundyRole;
import com.fundy.FundyBE.domain.user.repository.FundyUser;
import com.fundy.FundyBE.domain.user.repository.UserRepository;
import com.fundy.FundyBE.domain.user.service.dto.request.LoginServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.request.SignUpServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.request.VerifyEmailCodeServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.response.AvailableNicknameResponse;
import com.fundy.FundyBE.domain.user.service.dto.response.EmailCodeResponse;
import com.fundy.FundyBE.domain.user.service.dto.response.UserInfoResponse;
import com.fundy.FundyBE.domain.user.service.dto.response.VerifyEmailResponse;
import com.fundy.FundyBE.global.component.email.AsyncEmailSender;
import com.fundy.FundyBE.global.component.jwt.JwtProvider;
import com.fundy.FundyBE.global.component.jwt.TokenInfo;
import com.fundy.FundyBE.global.component.jwt.TokenType;
import com.fundy.FundyBE.global.config.redis.logoutInfo.LogoutInfo;
import com.fundy.FundyBE.global.config.redis.logoutInfo.LogoutInfoRedisRepository;
import com.fundy.FundyBE.global.config.redis.refreshInfo.RefreshInfo;
import com.fundy.FundyBE.global.config.redis.refreshInfo.RefreshInfoRedisRepository;
import com.fundy.FundyBE.global.exception.customException.DuplicateUserException;
import com.fundy.FundyBE.global.exception.customException.NoUserException;
import com.fundy.FundyBE.global.exception.customException.RefreshTokenException;
import com.fundy.FundyBE.global.validation.user.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    private final AsyncEmailSender emailSender;
    private final RefreshInfoRedisRepository refreshInfoRedisRepository;
    private final LogoutInfoRedisRepository logoutInfoRedisRepository;

    @Transactional
    public UserInfoResponse emailSignUp(@Valid final SignUpServiceRequest signUpServiceRequest) {
        userValidator.hasDuplicateEmail(signUpServiceRequest.getEmail());
        userValidator.hasDuplicateNickname(signUpServiceRequest.getNickname());

        FundyUser fundyUser = userRepository.save(FundyUser.builder()
                .email(signUpServiceRequest.getEmail())
                .password(passwordEncoder.encode(signUpServiceRequest.getPassword()))
                .nickname(
                        generateNicknameIsNull(signUpServiceRequest.getNickname()))
                .profileImage(
                        useBasicImageIsNull(signUpServiceRequest.getProfileImage()))
                .role(FundyRole.NORMAL_USER)
                .authType(AuthType.EMAIL)
                .build());

        return UserInfoResponse.builder()
                .id(fundyUser.getId().toString())
                .email(fundyUser.getEmail())
                .nickname(fundyUser.getNickname())
                .profileImage(fundyUser.getProfileImage())
                .role(fundyUser.getRole().getValue())
                .authProvider(fundyUser.getAuthType().getValue())
                .build();
    }

    public UserInfoResponse findByEmail(String email) {
        FundyUser fundyUser = userRepository.findByEmail(email).orElseThrow(() ->
                NoUserException.createBasic());
        return UserInfoResponse.builder()
                .id(fundyUser.getId().toString())
                .email(fundyUser.getEmail())
                .nickname(fundyUser.getNickname())
                .profileImage(fundyUser.getProfileImage())
                .role(fundyUser.getRole().getValue())
                .authProvider(fundyUser.getAuthType().getValue())
                .build();
    }

    @Transactional
    public TokenInfo login(@Valid final LoginServiceRequest loginServiceRequest) {
        // TODO: 다른 프로바이더 로그인 안되게 막기
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          loginServiceRequest.getEmail(),
          loginServiceRequest.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenInfo tokenInfo = jwtProvider.generateToken(authentication);
        refreshInfoRedisRepository.save(RefreshInfo.builder()
                .id(authentication.getName())
                .authorities(authentication.getAuthorities())
                .refreshToken(tokenInfo.getRefreshToken())
                .build());

        return jwtProvider.generateToken(authentication);
    }
    @Transactional
    public TokenInfo reissueToken(HttpServletRequest request) {
        String refreshToken = jwtProvider.resolveToken(request);

        if(refreshToken == null || !jwtProvider.isVerifyToken(refreshToken, TokenType.REFRESH))
            throw RefreshTokenException.createBasic();

        RefreshInfo refreshInfo = refreshInfoRedisRepository.findByRefreshToken(refreshToken).orElseThrow(RefreshTokenException::createBasic);
        TokenInfo tokenInfo = jwtProvider.generateTokenWithRefreshInfo(refreshInfo);
        refreshInfo.setRefreshToken(tokenInfo.getRefreshToken());
        refreshInfoRedisRepository.save(refreshInfo);

        return tokenInfo;
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        // accessToken 유효성 검사는 Filter에서 진웃
        String accessToken = jwtProvider.resolveToken(request);

        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        logoutInfoRedisRepository.save(LogoutInfo.builder()
                        .accessToken(accessToken)
                        .email(userDetails.getUsername())
                .build());

        refreshInfoRedisRepository.deleteById(userDetails.getUsername());
        // 로그아웃 절차
        // 1. 로그아웃 시도
        // 2. 토큰 유효성 검사
        // 3. 액세스토큰 블랙리스트 등록
        // 4. 리프레쉬토큰 삭제
        // 5. 정상적인 작동
        // 6. 블랙리스트 액세스토큰 검사
    }

    public EmailCodeResponse sendEmailCodeAndReturnToken(String email){
        userValidator.hasDuplicateEmail(email);
        String code = generateCode();
        String token = jwtProvider.generateEmailVerifyToken(email, code);
        emailSender.sendEmailCode(email, code);

        return EmailCodeResponse.builder()
                .email(email)
                .token(token)
                .build();
    }

    public VerifyEmailResponse verifyTokenWithEmail(@Valid final VerifyEmailCodeServiceRequest verifyEmailCodeServiceRequest) {
        return VerifyEmailResponse.builder()
                .email(verifyEmailCodeServiceRequest.getEmail())
                .verify(jwtProvider.isVerifyEmailTokenWithCode(
                        verifyEmailCodeServiceRequest.getToken(),
                        verifyEmailCodeServiceRequest.getEmail(),
                        verifyEmailCodeServiceRequest.getCode()
                ))
                .build();
    }

    public AvailableNicknameResponse isAvailableNickname(String nickname) {
        try {
            userValidator.hasDuplicateNickname(nickname);
        } catch (DuplicateUserException e) {
            return AvailableNicknameResponse.builder()
                    .nickname(nickname)
                    .available(false)
                    .build();
        }

        return AvailableNicknameResponse.builder()
                .nickname(nickname)
                .available(true)
                .build();
    }

    private String generateNicknameIsNull(String nickname) {
        if(nickname == null) {
            int RANDOM_STRING_LENGTH = 6;
            String newNickname = "유저-"+generateRandomString(RANDOM_STRING_LENGTH);
            while(userRepository.findByNickname(newNickname).isPresent()) {
                newNickname = "유저-"+generateRandomString(RANDOM_STRING_LENGTH);
            }
            return newNickname;
        }

        return nickname;
    }
    private String generateCode() {
        int codeSize = 6;
        String code = "";
        String numericCharacters = "0123456789";
        Random random = new Random();
        for(int i=0;i<codeSize;i++) {
            code += numericCharacters.charAt(random.nextInt(numericCharacters.length()));
        }
        return code;
    }

    private String useBasicImageIsNull(String url) {
        if(url == null) {
            // TODO: 기본 URL을 사진 확정되면 바꾸어야함.
            String BASIC_URL = "https://austinpeopleworks.com/wp-content/uploads/2020/12/blank-profile-picture-973460_1280.png";
            return BASIC_URL;
        }
        return url;
    }

    private String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        String randomString = "";
        for(int i=0;i<length;i++)
            randomString += characters.charAt(random.nextInt(characters.length()));

        return randomString;
    }
}
