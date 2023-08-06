package com.fundy.FundyBE.global.jwt;

import com.fundy.FundyBE.global.config.redis.refreshInfo.RefreshInfo;
import com.fundy.FundyBE.global.config.redis.refreshInfo.RefreshTokenRedisRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JwtRedisTest {
    @Autowired
    RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Test
    void save() {
        RefreshInfo refreshInfo = RefreshInfo.builder()
                .id("dongwon0103@naver.com")
                .refreshToken("abcd")
                .build();

        RefreshInfo saveRefreshInfo = refreshTokenRedisRepository.save(refreshInfo);
        Assertions.assertThat(saveRefreshInfo).isEqualTo(refreshInfo);
        System.out.println(saveRefreshInfo.getId());
    }

    @Test
    void getRefreshToken() {
        String email = "dongwon0103@naver.com";

        RefreshInfo refreshInfo = RefreshInfo.builder()
                .id(email)
                .refreshToken("abcd")
                .build();

        RefreshInfo save = refreshTokenRedisRepository.save(refreshInfo);
        RefreshInfo findToken = refreshTokenRedisRepository.findByRefreshToken(save.getRefreshToken()).orElseThrow();
        System.out.println(findToken.getId());
        System.out.println(findToken.getRefreshToken());
    }
}
