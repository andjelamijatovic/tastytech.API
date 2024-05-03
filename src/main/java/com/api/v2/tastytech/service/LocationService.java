package com.api.v2.tastytech.service;

import com.api.v2.tastytech.dto.LocationInputDto;
import com.api.v2.tastytech.dto.LocationOutputDto;

import java.util.List;

public interface LocationService {

    LocationOutputDto save(LocationInputDto locationDto) throws Exception;
    List<LocationOutputDto> findAll(Long brandId) throws Exception;
    LocationOutputDto findById(Long id) throws Exception;
    LocationOutputDto update(Long id, LocationInputDto locationDto) throws Exception;
    void delete(Long id) throws Exception;
}
