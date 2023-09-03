package com.fundy.FundyBE.domain.project.repository.converter;

import com.fundy.FundyBE.global.constraint.Day;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DayAttributeConverter implements AttributeConverter<Day, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Day attribute) {
        return switch (attribute) {
            case MONDAY -> 0;
            case TUESDAY -> 1;
            case WEDNESDAY -> 2;
            case THURSDAY -> 3;
            case FRIDAY -> 4;
            case SATURDAY -> 5;
            case SUNDAY -> 6;
        };
    }

    @Override
    public Day convertToEntityAttribute(Integer dbData) {
        return switch (dbData) {
            case 0 -> Day.MONDAY;
            case 1 -> Day.TUESDAY;
            case 2 -> Day.WEDNESDAY;
            case 3 -> Day.THURSDAY;
            case 4 -> Day.FRIDAY;
            case 5 -> Day.SATURDAY;
            case 6 -> Day.SUNDAY;
            default -> null;
        };
    }
}
