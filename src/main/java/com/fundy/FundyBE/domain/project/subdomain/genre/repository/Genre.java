package com.fundy.FundyBE.domain.project.subdomain.genre.repository;

import com.fundy.FundyBE.domain.project.repository.Project;
import com.fundy.FundyBE.global.constraint.GenreName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GENRE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "NAME", nullable = false, length = 10)
    private GenreName name;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @Builder
    private Genre(GenreName name, Project project) {
        this.name = name;
        this.project = project;
    }
}
