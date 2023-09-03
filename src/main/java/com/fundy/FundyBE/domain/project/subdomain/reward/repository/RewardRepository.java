package com.fundy.FundyBE.domain.project.subdomain.reward.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    Reward save(Reward reward);
    List<Reward> findByProjectId(Long id);
}
