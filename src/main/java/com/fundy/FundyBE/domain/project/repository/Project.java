package com.fundy.FundyBE.domain.project.repository;

import com.fundy.FundyBE.domain.common.BaseTimeEntity;
import com.fundy.FundyBE.domain.project.repository.converter.BooleanAttributeConverter;
import com.fundy.FundyBE.domain.project.subdomain.genre.repository.Genre;
import com.fundy.FundyBE.domain.user.repository.FundyUser;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @ElementCollection
    @CollectionTable(name = "PROJECT_SUBMEDIA")
    @Column(name = "MEDIA_URL")
    private List<String> subMedias;

    @Lob
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "SUB_DESCRIPTION", nullable = false)
    private String subDescription;

    @Embedded
    private ProjectPeriod projectPeriod;

    @Embedded
    private DevNoteUploadTerm devNoteUploadTerm;

    @Convert(converter = BooleanAttributeConverter.class)
    @Column(name = "IS_PROMOTION", nullable = false, length = 1)
    private boolean isPromotion;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private FundyUser user;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    private List<Genre> genres = new ArrayList<>(5);

    @Builder
    private Project(String name, String thumbnail, List<String> subMedias, String description,
                    ProjectPeriod projectPeriod, DevNoteUploadTerm devNoteUploadTerm, FundyUser user,
                    boolean isPromotion, String subDescription) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.subMedias = subMedias;
        this.description = description;
        this.projectPeriod = projectPeriod;
        this.devNoteUploadTerm = devNoteUploadTerm;
        this.user = user;
        this.isPromotion = isPromotion;
        this.subDescription = subDescription;
    }
}
