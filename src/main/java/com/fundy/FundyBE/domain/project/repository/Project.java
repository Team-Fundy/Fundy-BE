package com.fundy.FundyBE.domain.project.repository;

import com.fundy.FundyBE.domain.project.subdomain.genre.repository.Genre;
import com.fundy.FundyBE.domain.user.repository.FundyUser;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROJECT")
@NoArgsConstructor
@Getter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "PROJECT_MAIN_IMAGES")
    @Column(name = "IMAGE_URL")
    private List<String> mainImages;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @Embedded
    private ProjectPeriod projectPeriod;

    @Embedded
    private DevNoteUploadTerm devNoteUploadTerm;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private FundyUser user;

    @OneToMany(mappedBy = "project")
    private List<Genre> genres = new ArrayList<>(5);

    @Builder
    private Project(String name, List<String> mainImages, String description, ProjectPeriod projectPeriod, DevNoteUploadTerm devNoteUploadTerm, FundyUser user) {
        this.name = name;
        this.mainImages = mainImages;
        this.description = description;
        this.projectPeriod = projectPeriod;
        this.devNoteUploadTerm = devNoteUploadTerm;
        this.user = user;
    }
}
