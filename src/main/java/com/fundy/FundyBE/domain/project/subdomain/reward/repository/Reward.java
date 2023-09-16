package com.fundy.FundyBE.domain.project.subdomain.reward.repository;

import com.fundy.FundyBE.domain.project.repository.Project;
import com.fundy.FundyBE.domain.project.subdomain.reward.repository.converter.ItemsAttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "MINIMUM_PRICE", nullable = false)
    private int minimumPrice;

    @Convert(converter = ItemsAttributeConverter.class)
    @Column(name = "ITEMS")
    private List<String> items;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @Builder
    private Reward(String name, int minimumPrice, List<String> items, String image, Project project) {
        this.name = name;
        this.minimumPrice = minimumPrice;
        this.items = items;
        this.image = image;
        this.project = project;
    }
}
