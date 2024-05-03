package com.api.v2.tastytech.service;

import com.api.v2.tastytech.dto.CategoryInputDto;
import com.api.v2.tastytech.dto.CategoryOutputDto;

import java.util.List;

public interface CategoryService {

    CategoryOutputDto save(Long id, CategoryInputDto categoryDto) throws Exception;
    List<CategoryOutputDto> getAll(Long id) throws Exception;
    CategoryOutputDto getById(Long id) throws Exception;
    CategoryOutputDto update(Long id, CategoryInputDto categoryDto) throws Exception;
    void delete(Long id) throws Exception;
}
