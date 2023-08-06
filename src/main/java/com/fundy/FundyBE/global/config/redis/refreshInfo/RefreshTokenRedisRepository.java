package com.fundy.FundyBE.global.config.redis.refreshInfo;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshInfo, String> {
    RefreshInfo save(RefreshInfo token);
    Optional<RefreshInfo> findById(String email);
    Optional<RefreshInfo> findByRefreshToken(String token);
}
