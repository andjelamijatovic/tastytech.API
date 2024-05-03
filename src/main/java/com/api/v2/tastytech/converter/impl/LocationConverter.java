package com.api.v2.tastytech.converter.impl;

import com.api.v2.tastytech.converter.DtoEntityConverter;
import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.domain.Location;
import com.api.v2.tastytech.dto.LocationInputDto;
import com.api.v2.tastytech.dto.LocationOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class LocationConverter implements DtoEntityConverter<LocationInputDto, LocationOutputDto, Location> {

    @Autowired
    private BrandConverter brandConverter;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocationOutputDto toDto(Location entity) {
        return new LocationOutputDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getLongitude(),
                entity.getLatitude(),
                entity.getPhone(),
                entity.getAddress(),
                brandConverter.toDto(entity.getBrand())
        );
    }

    @Override
    public Location toEntity(LocationInputDto input) throws Exception {
        if(input.getBrandId() == null) {
            throw new Exception("Brand id is required!");
        }
        return new Location(
                null,
                sdf.parse(sdf.format(new Date())),
                sdf.parse(sdf.format(new Date())),
                input.getName(),
                input.getDescription(),
                input.getLongitude(),
                input.getLatitude(),
                input.getPhone(),
                input.getAddress(),
                new Brand(input.getBrandId(), null, null, null)
        );
    }
}
