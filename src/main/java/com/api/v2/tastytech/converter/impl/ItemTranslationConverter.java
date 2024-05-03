package com.api.v2.tastytech.converter.impl;

import com.api.v2.tastytech.converter.DtoEntityConverter;
import com.api.v2.tastytech.domain.ItemTranslation;
import com.api.v2.tastytech.dto.ItemTranslationInputDto;
import com.api.v2.tastytech.dto.ItemTranslationOutputDto;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ItemTranslationConverter implements DtoEntityConverter<ItemTranslationInputDto, ItemTranslationOutputDto, ItemTranslation> {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        return new ItemTranslation(
                null,
                sdf.parse(sdf.format(new Date())),
                sdf.parse(sdf.format(new Date())),
                input.getItem(),
                input.getDescription(),
                null,
                null
        );
    }
}
