package com.fundy.FundyBE.domain.project.subdomain.reward.service.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SaveRewardServiceRequest {
    private String name;
    private int minimumPrice;
    private String description;
    private String image;
}
