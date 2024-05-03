package com.api.v2.tastytech.converter.impl;

import com.api.v2.tastytech.converter.DtoEntityConverter;
import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.dto.BrandInputDto;
import com.api.v2.tastytech.dto.BrandOutputDto;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BrandConverter implements DtoEntityConverter<BrandInputDto, BrandOutputDto, Brand> {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public BrandOutputDto toDto(Brand entity) {
        return new BrandOutputDto(
                entity.getId(),
                entity.getName()
        );
    }

    @Override
    public Brand toEntity(BrandInputDto input) throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));
        return new Brand(
                null,
                input.getName(),
                formatedDate,
                formatedDate
        );
    }
}
