package com.api.v2.tastytech.service;

import com.api.v2.tastytech.dto.ItemInputDto;
import com.api.v2.tastytech.dto.ItemOutputDto;

import java.util.List;

public interface ItemService {

    ItemOutputDto save(Long id, ItemInputDto itemDto) throws Exception;
    List<ItemOutputDto> getAll(Long id) throws Exception;
    ItemOutputDto getById(Long id) throws Exception;
    ItemOutputDto update(Long id, ItemInputDto itemDto) throws Exception;
    void delete(Long id) throws Exception;
}
