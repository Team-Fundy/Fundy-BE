package com.fundy.FundyBE.global.config.redis.refreshInfo;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshInfoRedisRepository extends CrudRepository<RefreshInfo, String> {
    RefreshInfo save(RefreshInfo refreshInfo);
    Optional<RefreshInfo> findById(String id);
    Optional<RefreshInfo> findByRefreshToken(String refreshToken);
}
