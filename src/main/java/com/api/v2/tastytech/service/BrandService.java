package com.api.v2.tastytech.service;

import com.api.v2.tastytech.dto.BrandInputDto;
import com.api.v2.tastytech.dto.BrandOutputDto;

import java.util.List;

public interface BrandService {

    BrandOutputDto save(BrandInputDto brandDto) throws Exception;
    List<BrandOutputDto> findAll();
    BrandOutputDto findById(Long id) throws Exception;
    BrandOutputDto update(Long id, BrandInputDto brandDto) throws Exception;
    void delete(Long id) throws Exception;

}
