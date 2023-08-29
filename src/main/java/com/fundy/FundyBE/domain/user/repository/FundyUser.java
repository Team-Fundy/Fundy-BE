package com.fundy.FundyBE.domain.user.repository;

import com.fundy.FundyBE.domain.user.repository.converter.AuthTypeAttributeConverter;
import com.fundy.FundyBE.domain.user.repository.converter.RoleAttributeConverter;
import com.fundy.FundyBE.global.constraint.AuthType;
import com.fundy.FundyBE.global.constraint.FundyRole;
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
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PROFILE_IMAGE")
    private String profileImage;
    @Convert(converter = RoleAttributeConverter.class)
    @Column(name = "ROLE", nullable = false)
    private FundyRole role;

    @Convert(converter = AuthTypeAttributeConverter.class)
    @Column(name = "AUTH_TYPE", nullable = false)
    private AuthType authType;

    @Builder
    private FundyUser(String nickname, String password, String email, String profileImage, FundyRole role, AuthType authType) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
        this.role = role;
        this.authType = authType;
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

    public AuthType getAuthType() {
        return authType;
    }

    public void setRole(FundyRole role) {
        this.role = role;
    }
}
