package com.api.v2.tastytech.converter.impl;

import com.api.v2.tastytech.converter.DtoEntityConverter;
import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.domain.Menu;
import com.api.v2.tastytech.dto.MenuInputDto;
import com.api.v2.tastytech.dto.MenuOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class MenuConverter implements DtoEntityConverter<MenuInputDto, MenuOutputDto, Menu> {

    @Autowired
    private BrandConverter brandConverter;
    @Autowired
    private CategoryConverter categoryConverter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public MenuOutputDto toDto(Menu entity) {
        return new MenuOutputDto(
                entity.getId(),
                entity.getName(),
                brandConverter.toDto(entity.getBrand()),
                (entity.getCategories() != null)
                        ? (entity.getCategories()
                        .stream()
                        .map(category -> categoryConverter.toDto(category))
                        .collect(Collectors.toList()))
                        : null
        );
    }

    @Override
    public Menu toEntity(MenuInputDto input) throws Exception {
        return new Menu(
                null,
                sdf.parse(sdf.format(new Date())),
                sdf.parse(sdf.format(new Date())),
                input.getName(),
                new Brand(input.getBrandId(), null, null, null),
                null
        );
    }
}
