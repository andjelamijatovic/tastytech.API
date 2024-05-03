package com.api.v2.tastytech.service.impl;

import com.api.v2.tastytech.dto.CategoryInputDto;
import com.api.v2.tastytech.dto.CategoryOutputDto;
import com.api.v2.tastytech.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    // TODO: implement all service methods
    @Override
    public CategoryOutputDto save(Long id, CategoryInputDto categoryDto) throws Exception {
        return null;
    }

    @Override
    public List<CategoryOutputDto> getAll(Long id) throws Exception {
        return null;
    }

    @Override
    public CategoryOutputDto getById(Long id) throws Exception {
        return null;
    }

    @Override
    public CategoryOutputDto update(Long id, CategoryInputDto categoryDto) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

}
