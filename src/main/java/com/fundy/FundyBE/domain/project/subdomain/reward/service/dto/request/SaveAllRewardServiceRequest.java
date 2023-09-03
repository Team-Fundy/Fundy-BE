package com.fundy.FundyBE.domain.project.subdomain.reward.service.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SaveAllRewardServiceRequest {
    private Long projectId;
    private List<SaveRewardServiceRequest> rewards;
}
