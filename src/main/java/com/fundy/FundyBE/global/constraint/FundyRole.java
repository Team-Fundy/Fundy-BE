package com.fundy.FundyBE.global.constraint;

public enum FundyRole {
    NORMAL_USER("NORMAL_USER"),
    CREATOR("CREATOR"),
    ADMIN("ADMIN"),
    GUEST("GUEST");

    private String value;

    FundyRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
