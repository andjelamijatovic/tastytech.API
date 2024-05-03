package com.api.v2.tastytech.converter.impl;

import com.api.v2.tastytech.converter.DtoEntityConverter;
import com.api.v2.tastytech.domain.CategoryTranslation;
import com.api.v2.tastytech.dto.CategoryTranslationInputDto;
import com.api.v2.tastytech.dto.CategoryTranslationOutputDto;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CategoryTranslationConverter implements DtoEntityConverter<CategoryTranslationInputDto, CategoryTranslationOutputDto, CategoryTranslation> {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public CategoryTranslationOutputDto toDto(CategoryTranslation entity) {
        return new CategoryTranslationOutputDto(
                entity.getTranslation(),
                entity.getDescription(),
                entity.getLanguage().getLanguage(),
                entity.getLanguage().getCulturalCode()
        );
    }

    @Override
    public CategoryTranslation toEntity(CategoryTranslationInputDto input) throws Exception {
        return new CategoryTranslation(
                null,
                sdf.parse(sdf.format(new Date())),
                sdf.parse(sdf.format(new Date())),
                input.getTranslation(),
                input.getDescription(),
                null,
                null
        );
    }
}
