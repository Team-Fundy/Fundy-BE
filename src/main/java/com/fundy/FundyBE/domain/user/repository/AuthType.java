package com.fundy.FundyBE.domain.user.repository;

public enum AuthType {
    EMAIL("EMAIL"),
    GOOGLE("GOOGLE"),
    NAVER("NAVER"),
    KAKAO("KAKAO");
    private String value;
    AuthType(String value) {
        this.value = value;
    }

    public String getValue() { return value; }
}
