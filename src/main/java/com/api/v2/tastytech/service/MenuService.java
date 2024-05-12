package com.api.v2.tastytech.service;

import com.api.v2.tastytech.dto.MenuInputDto;
import com.api.v2.tastytech.dto.MenuOutputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuService {

    MenuOutputDto save(MenuInputDto menuDto) throws Exception;
    List<MenuOutputDto> getAll(Long id) throws Exception;
    Page<MenuOutputDto> getAll(Long id, Pageable pageable) throws Exception;
    MenuOutputDto getById(Long id) throws Exception;
    MenuOutputDto update(Long id, MenuInputDto menuDto) throws Exception;
    void delete(Long id) throws Exception;
}
