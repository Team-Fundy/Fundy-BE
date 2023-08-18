package com.fundy.FundyBE.global.config.redis.logoutInfo;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@RedisHash(value = "logout", timeToLive = (2 * 60 * 60))
public class LogoutInfo {
    @Id
    private String id; // email -> id로 설정해야 에러 안남
    @Indexed
    private String email;
    @Indexed
    private String accessToken;
}
