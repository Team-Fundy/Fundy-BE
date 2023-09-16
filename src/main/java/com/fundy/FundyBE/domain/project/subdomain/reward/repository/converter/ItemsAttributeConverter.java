package com.fundy.FundyBE.domain.project.subdomain.reward.repository.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
@Converter
public class ItemsAttributeConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        StringBuilder sb = new StringBuilder();

        sb.append(attribute.get(0));
        for(int i=1;i<attribute.size();i++) {
            sb.append(", ").append(attribute.get(i));
        }

        return sb.toString();
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(", ")).toList();
    }
}
