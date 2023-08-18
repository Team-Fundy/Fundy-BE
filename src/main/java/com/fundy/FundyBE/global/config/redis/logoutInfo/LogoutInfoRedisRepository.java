package com.fundy.FundyBE.global.config.redis.logoutInfo;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LogoutInfoRedisRepository extends CrudRepository<LogoutInfo, String> {
    LogoutInfo save(LogoutInfo logoutInfo);
    Optional<LogoutInfo> findByAccessToken(String accessToken);
}
