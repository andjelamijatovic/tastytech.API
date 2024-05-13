package com.api.v2.tastytech.converter.impl;

import com.api.v2.tastytech.converter.DtoEntityConverter;
import com.api.v2.tastytech.domain.CategoryTranslation;
import com.api.v2.tastytech.domain.Language;
import com.api.v2.tastytech.dto.CategoryTranslationInputDto;
import com.api.v2.tastytech.dto.CategoryTranslationOutputDto;
import com.api.v2.tastytech.repository.LanguageRepository;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
public class CategoryTranslationConverter implements DtoEntityConverter<CategoryTranslationInputDto, CategoryTranslationOutputDto, CategoryTranslation> {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final LanguageRepository languageRepository;

    public CategoryTranslationConverter(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

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
        Optional<Language> language = languageRepository.findLanguageByCulturalCode(input.getCulturalCode());
        if(language.isEmpty()) {
            throw new Exception("Unknown language!");
        }

        return new CategoryTranslation(
                null,
                sdf.parse(sdf.format(new Date())),
                sdf.parse(sdf.format(new Date())),
                input.getTranslation(),
                input.getDescription(),
                language.get(),
                null
        );
    }
}
