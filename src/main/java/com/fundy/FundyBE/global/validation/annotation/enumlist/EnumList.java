package com.fundy.FundyBE.global.validation.annotation.enumlist;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumListValidator.class})
public @interface EnumList {
    String message() default "Enum에 없는 값입니다";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    Class<? extends java.lang.Enum<?>> enumClass();
    boolean ignoreCase() default false;
}
