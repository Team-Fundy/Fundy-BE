package com.fundy.FundyBE.domain.user.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "FUNDY_USER")
@NoArgsConstructor
//User라는 Spring Security 객체가 있기 때문에
public class FundyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NICKNAME", nullable = false, unique = true)
    private String nickname;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PROFILE_IMAGE")
    private String profileImage;
    @Convert(converter = RoleAttributeConverter.class)
    @Column(name = "ROLE", nullable = false)
    private FundyRole role;

    @Builder
    private FundyUser(String nickname, String password, String email, String profileImage, FundyRole role) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
        this.role = role;
    }

    public UUID getId() {
        return this.id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getEmail() {
        return this.email;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public String getPassword() {return this.password;}

    public FundyRole getRole() {
        return role;
    }
}
