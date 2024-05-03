package com.api.v2.tastytech.converter.impl;

import com.api.v2.tastytech.converter.DtoEntityConverter;
import com.api.v2.tastytech.domain.Item;
import com.api.v2.tastytech.domain.ItemTranslation;
import com.api.v2.tastytech.dto.ItemInputDto;
import com.api.v2.tastytech.dto.ItemOutputDto;
import com.api.v2.tastytech.dto.ItemTranslationInputDto;
import com.api.v2.tastytech.dto.ItemTranslationOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ItemConverter implements DtoEntityConverter<ItemInputDto, ItemOutputDto, Item> {

    @Autowired
    private ItemTranslationConverter itemTranslationConverter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ItemOutputDto toDto(Item entity) {

        List<ItemTranslationOutputDto> translations = new ArrayList<>();

        if(entity.getTranslations() != null && !entity.getTranslations().isEmpty()) {
            translations = entity.getTranslations()
                    .stream()
                    .map(e -> itemTranslationConverter.toDto(e))
                    .toList();
        }
        return new ItemOutputDto(
                entity.getId(),
                entity.getPrice(),
                translations
        );
    }

    @Override
    public Item toEntity(ItemInputDto input) throws Exception {

        List<ItemTranslation> translations = new ArrayList<>();
        if(input.getTranslations() != null && !input.getTranslations().isEmpty()) {
            for(ItemTranslationInputDto tr: input.getTranslations()) {
                ItemTranslation translation = itemTranslationConverter.toEntity(tr);
                translations.add(translation);
            }
        }

        return new Item(
                null,
                sdf.parse(sdf.format(new Date())),
                sdf.parse(sdf.format(new Date())),
                input.getPrice(),
                null,
                translations
        );
    }
}
