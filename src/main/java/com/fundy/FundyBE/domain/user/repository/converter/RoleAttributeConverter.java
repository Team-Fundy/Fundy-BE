package com.fundy.FundyBE.domain.user.repository.converter;

import com.fundy.FundyBE.global.constraint.FundyRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleAttributeConverter implements AttributeConverter<FundyRole, Integer> {
    @Override
    public Integer convertToDatabaseColumn(FundyRole attribute) {
        if(attribute.equals(FundyRole.NORMAL_USER)) {
            return 0;
        }

        if(attribute.equals(FundyRole.CREATOR)) {
            return 1;
        }

        if(attribute.equals(FundyRole.ADMIN)) {
            return 2;
        }

        if(attribute.equals(FundyRole.GUEST)) {
            return 3;
        }
        return null;
    }

    @Override
    public FundyRole convertToEntityAttribute(Integer dbData) {
        if(dbData == 0) {
            return FundyRole.NORMAL_USER;
        }

        if(dbData == 1) {
            return FundyRole.CREATOR;
        }

        if(dbData == 2) {
            return FundyRole.ADMIN;
        }

        if(dbData == 3) {
            return FundyRole.GUEST;
        }

        return null;
    }
}
