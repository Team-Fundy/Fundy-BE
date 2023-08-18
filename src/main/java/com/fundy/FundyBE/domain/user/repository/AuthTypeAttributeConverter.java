package com.fundy.FundyBE.domain.user.repository;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AuthTypeAttributeConverter implements AttributeConverter<AuthType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AuthType attribute) {
        if(attribute.equals(AuthType.EMAIL)) {
            return 0;
        }

        if(attribute.equals(AuthType.GOOGLE)) {
            return 1;
        }

        if(attribute.equals(AuthType.NAVER)) {
            return 2;
        }

        if(attribute.equals(AuthType.KAKAO)) {
            return 3;
        }

        return null;
    }

    @Override
    public AuthType convertToEntityAttribute(Integer dbData) {
        if(dbData == 0) {
            return AuthType.EMAIL;
        }

        if(dbData == 1) {
            return AuthType.GOOGLE;
        }

        if(dbData == 2) {
            return AuthType.NAVER;
        }

        if(dbData == 3) {
            return AuthType.KAKAO;
        }
        return null;
    }
}
