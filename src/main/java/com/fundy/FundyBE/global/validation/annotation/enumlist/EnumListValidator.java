package com.fundy.FundyBE.global.validation.annotation.enumlist;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class EnumListValidator implements ConstraintValidator<EnumList, List<String>> {
    private EnumList annotation;
    @Override
    public void initialize(EnumList constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        Object[] enumValues = annotation.enumClass().getEnumConstants();
        if (enumValues == null)
            return false;

        for(String value : values) {
            if(!isIn(enumValues, value)) {
                return false;
            }
        }

        return true;
    }

    private boolean isIn(Object[] enumValues, String target) {
        if(annotation.ignoreCase()) {
            return isInIgnoreCase(enumValues, target);
        }

        return isInNormalCase(enumValues, target);
    }

    private boolean isInIgnoreCase(Object[] enumValues, String target) {
        for(Object enumValue : enumValues) {
            if(target.equalsIgnoreCase(enumValue.toString())) {
                return true;
            }
        }

        return false;
    }

    private boolean isInNormalCase(Object[] enumValues, String target) {
        for(Object enumValue : enumValues) {
            if(target.equals(enumValue.toString())) {
                return true;
            }
        }

        return false;
    }
}
