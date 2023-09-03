package com.fundy.FundyBE.domain.project.subdomain.genre.service;

import com.fundy.FundyBE.domain.project.repository.Project;
import com.fundy.FundyBE.domain.project.repository.ProjectRepository;
import com.fundy.FundyBE.domain.project.subdomain.genre.repository.Genre;
import com.fundy.FundyBE.domain.project.subdomain.genre.repository.GenreRepository;
import com.fundy.FundyBE.domain.project.subdomain.genre.service.dto.request.SaveAllGenresServiceRequest;
import com.fundy.FundyBE.global.constraint.GenreName;
import com.fundy.FundyBE.global.exception.customexception.NoProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {
    private final GenreRepository genreRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void saveAllGenres(final SaveAllGenresServiceRequest request) {
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(NoProjectException::createBasic);

        genreRepository.saveAll(request.getGenreNames().stream().map((genreName) ->
                        Genre.builder()
                                .name(genreName)
                                .project(project)
                                .build())
                .collect(Collectors.toList()));
    }

    public List<String> getAllGenres() {
        return Arrays.stream(GenreName.values())
                .map(GenreName::getValue)
                .collect(Collectors.toList());
    }
}
