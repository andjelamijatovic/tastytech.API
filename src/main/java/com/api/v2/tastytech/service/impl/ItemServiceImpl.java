package com.api.v2.tastytech.service.impl;

import com.api.v2.tastytech.converter.impl.ItemConverter;
import com.api.v2.tastytech.domain.Category;
import com.api.v2.tastytech.domain.Item;
import com.api.v2.tastytech.domain.ItemTranslation;
import com.api.v2.tastytech.dto.ItemInputDto;
import com.api.v2.tastytech.dto.ItemOutputDto;
import com.api.v2.tastytech.repository.CategoryRepository;
import com.api.v2.tastytech.repository.ItemRepository;
import com.api.v2.tastytech.repository.ItemTranslationRepository;
import com.api.v2.tastytech.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemTranslationRepository itemTranslationRepository;
    private final ItemConverter itemConverter;

    public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository,
                           ItemTranslationRepository itemTranslationRepository, ItemConverter itemConverter) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.itemTranslationRepository = itemTranslationRepository;
        this.itemConverter = itemConverter;
    }

    @Override
    public ItemOutputDto save(Long id, ItemInputDto itemDto) throws Exception {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            throw new Exception("Selected category doesn't exist!");
        }

        Item itemForSave = itemConverter.toEntity(itemDto);
        itemForSave.setCategory(category.get());

        for (ItemTranslation translation : itemForSave.getTranslations()) {
            translation.setItem(itemForSave);
        }

        Item savedItem = itemRepository.save(itemForSave);

        return itemConverter.toDto(savedItem);
    }

    @Override
    public List<ItemOutputDto> getAll(Long id) throws Exception {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            throw new Exception("Selected category doesn't exist!");
        }
        return itemRepository
                .findItemsByCategory(category.get())
                .stream()
                .map(itemConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ItemOutputDto> getAll(Long id, Pageable pageable) throws Exception {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            throw new Exception("Selected category doesn't exist!");
        }
        return  itemRepository
                .findItemsByCategory(category.get(), pageable)
                .map(itemConverter::toDto);
    }

    @Override
    public ItemOutputDto getById(Long id) throws Exception {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new Exception("Item doesn't exist!");
        }
        return itemConverter.toDto(item.get());
    }

    @Override
    public ItemOutputDto update(Long id, ItemInputDto itemDto) throws Exception {
        // TODO: implement update method for Item entity
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new Exception("Item doesn't exist!");
        }
        itemRepository.delete(item.get());
    }

}
