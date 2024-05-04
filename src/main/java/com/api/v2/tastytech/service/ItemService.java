package com.api.v2.tastytech.service;

import com.api.v2.tastytech.dto.ItemInputDto;
import com.api.v2.tastytech.dto.ItemOutputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    ItemOutputDto save(Long id, ItemInputDto itemDto) throws Exception;
    List<ItemOutputDto> getAll(Long id) throws Exception;
    Page<ItemOutputDto> getAll(Long id, Pageable pageable) throws Exception;
    ItemOutputDto getById(Long id) throws Exception;
    ItemOutputDto update(Long id, ItemInputDto itemDto) throws Exception;
    void delete(Long id) throws Exception;
}
