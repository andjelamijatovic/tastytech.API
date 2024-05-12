package com.api.v2.tastytech.converter.impl;

import com.api.v2.tastytech.converter.DtoEntityConverter;
import com.api.v2.tastytech.domain.ItemTranslation;
import com.api.v2.tastytech.domain.Language;
import com.api.v2.tastytech.dto.ItemTranslationInputDto;
import com.api.v2.tastytech.dto.ItemTranslationOutputDto;
import com.api.v2.tastytech.repository.LanguageRepository;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
public class ItemTranslationConverter implements DtoEntityConverter<ItemTranslationInputDto, ItemTranslationOutputDto, ItemTranslation> {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final LanguageRepository languageRepository;

    public ItemTranslationConverter(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public ItemTranslationOutputDto toDto(ItemTranslation entity) {
        return new ItemTranslationOutputDto(
                entity.getName(),
                entity.getDescription(),
                entity.getLanguage().getLanguage(),
                entity.getLanguage().getCulturalCode()
        );
    }

    @Override
    public ItemTranslation toEntity(ItemTranslationInputDto input) throws Exception {
        Optional<Language> language = languageRepository.findLanguageByCulturalCode(input.getCulturalCode());
        if(language.isEmpty()) {
            throw new Exception("Unknown language!");
        }

        return new ItemTranslation(
                null,
                sdf.parse(sdf.format(new Date())),
                sdf.parse(sdf.format(new Date())),
                input.getItem(),
                input.getDescription(),
                language.get(),
                null
        );
    }
}
