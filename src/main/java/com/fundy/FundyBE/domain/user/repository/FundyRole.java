package com.fundy.FundyBE.domain.user.repository;

public enum FundyRole {
    NORMAL_USER("NORMAL_USER"),
    CREATOR("CREATOR"),
    ADMIN("ADMIN");

    private String value;

    FundyRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
