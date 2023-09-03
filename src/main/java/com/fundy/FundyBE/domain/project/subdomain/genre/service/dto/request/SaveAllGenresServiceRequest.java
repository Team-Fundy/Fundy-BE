package com.fundy.FundyBE.domain.project.subdomain.genre.service.dto.request;

import com.fundy.FundyBE.global.constraint.GenreName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class SaveAllGenresServiceRequest {
    private Long projectId;
    private List<GenreName> genreNames;
}
