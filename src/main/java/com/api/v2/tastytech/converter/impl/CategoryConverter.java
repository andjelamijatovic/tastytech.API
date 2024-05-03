package com.api.v2.tastytech.converter.impl;

import com.api.v2.tastytech.converter.DtoEntityConverter;
import com.api.v2.tastytech.domain.Category;
import com.api.v2.tastytech.domain.CategoryTranslation;
import com.api.v2.tastytech.dto.CategoryInputDto;
import com.api.v2.tastytech.dto.CategoryOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryConverter implements DtoEntityConverter<CategoryInputDto, CategoryOutputDto, Category> {

    @Autowired
    private CategoryTranslationConverter categoryTranslationConverter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public CategoryOutputDto toDto(Category entity) {
        return new CategoryOutputDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                (entity.getParentCategory() != null) ? entity.getParentCategory().getName() : null,
                (entity.getTranslations() != null)
                        ? (entity.getTranslations()
                        .stream()
                        .map(translation -> categoryTranslationConverter.toDto(translation))
                        .collect(Collectors.toList()))
                        : null,
                    null);
    }

    @Override
    public Category toEntity(CategoryInputDto input) throws Exception {

        // TODO: add logic to transform CategoryTranslation
        List<CategoryTranslation> translations = new ArrayList<>();

        return new Category(
                null,
                input.getName(),
                input.getDescription(),
                sdf.parse(sdf.format(new Date())),
                sdf.parse(sdf.format(new Date())),
                null,
                null,
                translations,
                null);
    }
}
