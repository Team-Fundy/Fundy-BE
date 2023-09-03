package com.fundy.FundyBE.domain.project.service.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProjectRewardServiceRequest {
    private String name;
    private String image;
    private String description;
    private int minimumPrice;
}
