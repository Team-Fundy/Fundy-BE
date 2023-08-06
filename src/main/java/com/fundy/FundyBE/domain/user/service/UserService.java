package com.fundy.FundyBE.domain.user.service;

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
import com.fundy.FundyBE.global.exception.customException.DuplicateUserException;
import com.fundy.FundyBE.global.exception.customException.NoUserException;
import com.fundy.FundyBE.global.exception.customException.RefreshTokenException;
import com.fundy.FundyBE.global.component.jwt.JwtProvider;
import com.fundy.FundyBE.global.config.redis.refreshInfo.RefreshInfo;
import com.fundy.FundyBE.global.config.redis.refreshInfo.RefreshTokenRedisRepository;
import com.fundy.FundyBE.global.component.jwt.TokenInfo;
import com.fundy.FundyBE.global.validation.user.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final AsyncEmailSender emailSender;

    public final UserInfoResponse emailSignUp(@Valid final SignUpServiceRequest signUpServiceRequest) {
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
                .build());

        return UserInfoResponse.builder()
                .id(fundyUser.getId().toString())
                .email(fundyUser.getEmail())
                .nickname(fundyUser.getNickname())
                .profileImage(fundyUser.getProfileImage())
                .build();
    }

    public UserInfoResponse findByEmail(String email) {
        FundyUser fundyUser = userRepository.findByEmail(email).orElseThrow(NoUserException::createBasic);
        return UserInfoResponse.builder()
                .id(fundyUser.getId().toString())
                .email(fundyUser.getEmail())
                .nickname(fundyUser.getNickname())
                .profileImage(fundyUser.getProfileImage())
                .build();
    }

    @Transactional
    public TokenInfo login(@Valid final LoginServiceRequest loginServiceRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          loginServiceRequest.getEmail(),
          loginServiceRequest.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenInfo tokenInfo = jwtProvider.generateToken(authentication);
        refreshTokenRedisRepository.save(RefreshInfo.builder()
                .id(authentication.getName())
                .authorities(authentication.getAuthorities())
                .refreshToken(tokenInfo.getRefreshToken())
                .build());

        return tokenInfo;
    }

    public final EmailCodeResponse sendEmailCodeAndReturnToken(String email){
        userValidator.hasDuplicateEmail(email);
        String code = generateCode();
        String token = jwtProvider.generateEmailVerifyToken(email, code);
        emailSender.sendEmailCode(email, code);

        return EmailCodeResponse.builder()
                .email(email)
                .token(token)
                .build();
    }

    public final VerifyEmailResponse verifyTokenWithEmail(@Valid final VerifyEmailCodeServiceRequest verifyEmailCodeServiceRequest) {
        return VerifyEmailResponse.builder()
                .email(verifyEmailCodeServiceRequest.getEmail())
                .verify(jwtProvider.isVerifyEmailTokenWithCode(
                        verifyEmailCodeServiceRequest.getToken(),
                        verifyEmailCodeServiceRequest.getEmail(),
                        verifyEmailCodeServiceRequest.getCode()
                ))
                .build();
    }

    public final AvailableNicknameResponse isAvailableNickname(String nickname) {
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

    @Transactional
    public TokenInfo reissueToken(HttpServletRequest request) {
        String token = resolveToken(request);

        if(token == null || !jwtProvider.isVerifyToken(token))
            throw RefreshTokenException.createBasic();

        RefreshInfo refreshInfo = refreshTokenRedisRepository.findByRefreshToken(token).orElseThrow(RefreshTokenException::createBasic);
        TokenInfo tokenInfo = jwtProvider.generateToken(refreshInfo.getAuthorities(), refreshInfo.getId());
        refreshInfo.setRefreshToken(tokenInfo.getRefreshToken());
        refreshTokenRedisRepository.save(refreshInfo);

        return tokenInfo;
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
        StringBuilder code = new StringBuilder();
        String numericCharacters = "0123456789";
        Random random = new Random();
        for(int i=0;i<codeSize;i++) {
            code.append(numericCharacters.charAt(random.nextInt(numericCharacters.length())));
        }
        return code.toString();
    }

    private String useBasicImageIsNull(String url) {
        // FIXME: 기본 URL을 사진 확정되면 바꾸어야함.
        return Objects.requireNonNullElse(url, "https://austinpeopleworks.com/wp-content/uploads/2020/12/blank-profile-picture-973460_1280.png");
    }

    private String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        StringBuilder randomString = new StringBuilder();
        for(int i=0;i<length;i++)
            randomString.append(characters.charAt(random.nextInt(characters.length())));

        return randomString.toString();
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
