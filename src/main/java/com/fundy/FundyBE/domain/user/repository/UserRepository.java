package com.fundy.FundyBE.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<FundyUser, UUID> {
    FundyUser save(FundyUser fundyUser);
    Optional<FundyUser> findByEmail(String email);

    Optional<FundyUser> findByNickname(String nickname);

    Optional<FundyUser> findById(UUID uuid);
}
