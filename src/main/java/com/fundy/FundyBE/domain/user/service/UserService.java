package com.fundy.FundyBE.domain.user.service;

import com.fundy.FundyBE.domain.user.repository.FundyRole;
import com.fundy.FundyBE.domain.user.repository.FundyUser;
import com.fundy.FundyBE.domain.user.repository.UserRepository;
import com.fundy.FundyBE.domain.user.service.dto.request.LoginServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.request.SignUpServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.response.UserInfoServiceResponse;
import com.fundy.FundyBE.global.exception.customException.NoUserException;
import com.fundy.FundyBE.global.jwt.JwtProvider;
import com.fundy.FundyBE.global.jwt.TokenInfo;
import com.fundy.FundyBE.global.validation.user.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public final UserInfoServiceResponse emailSignUp(@Valid final SignUpServiceRequest signUpServiceRequest) {
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

        return UserInfoServiceResponse.builder()
                .id(fundyUser.getId().toString())
                .email(fundyUser.getEmail())
                .nickname(fundyUser.getNickname())
                .profileImage(fundyUser.getProfileImage())
                .build();
    }

    public UserInfoServiceResponse findByEmail(String email) {
        FundyUser fundyUser = userRepository.findByEmail(email).orElseThrow(() ->
                NoUserException.createBasic());
        return UserInfoServiceResponse.builder()
                .id(fundyUser.getId().toString())
                .email(fundyUser.getEmail())
                .nickname(fundyUser.getNickname())
                .profileImage(fundyUser.getProfileImage())
                .build();
    }

    public final TokenInfo login(@Valid final LoginServiceRequest loginServiceRequest) {
        log.debug("Call login service");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          loginServiceRequest.getEmail(),
          loginServiceRequest.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtProvider.generateToken(authentication);
    }

    private String generateNicknameIsNull(String nickname) {
        if(nickname == null) {
            int RANDOM_STRING_LENGTH = 6;
            String newNickname = "유저-"+generateRandomString(RANDOM_STRING_LENGTH);
            while(userRepository.findByNickname(newNickname).isPresent()) {
                newNickname = "유저-"+generateRandomString(RANDOM_STRING_LENGTH);
                System.out.println(userRepository.findByNickname(newNickname).isEmpty());
            }
            return newNickname;
        }

        return nickname;
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
