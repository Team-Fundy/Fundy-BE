package com.fundy.FundyBE.global.config.redis.refreshInfo;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@RedisHash(value = "refresh", timeToLive = (30 * 24 * 60 * 60))
public class RefreshInfo {
    @Id
    private String id; // email -> id로 설정해야 에러 안남
    private Collection<? extends GrantedAuthority> authorities;
    @Indexed
    private String refreshToken;

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
