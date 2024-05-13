package com.api.v2.tastytech.service.impl;

import com.api.v2.tastytech.converter.impl.CategoryConverter;
import com.api.v2.tastytech.converter.impl.CategoryTranslationConverter;
import com.api.v2.tastytech.domain.*;
import com.api.v2.tastytech.dto.CategoryInputDto;
import com.api.v2.tastytech.dto.CategoryOutputDto;
import com.api.v2.tastytech.repository.CategoryRepository;
import com.api.v2.tastytech.repository.CategoryTranslationRepository;
import com.api.v2.tastytech.repository.LanguageRepository;
import com.api.v2.tastytech.repository.MenuRepository;
import com.api.v2.tastytech.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;
    private final CategoryConverter categoryConverter;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MenuRepository menuRepository,
                               CategoryConverter categoryConverter) {
        this.categoryRepository = categoryRepository;
        this.menuRepository = menuRepository;
        this.categoryConverter = categoryConverter;
    }

    @Override
    public CategoryOutputDto save(Long id, CategoryInputDto categoryDto) throws Exception {
        // get menu from db
        Optional<Menu> menu = menuRepository.findById(id);
        if (menu.isEmpty()) {
            throw new Exception("Selected menu doesn't exist!");
        }
        // get parent category
        Optional<Category> parentCategory = null;
        if (categoryDto.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(categoryDto.getParentCategoryId());
            if (parentCategory.isEmpty()) {
                throw new Exception("Selected parent category doesn't exist!");
            }
        }
        // convert categoryDto to Entity
        Category categoryForSave = categoryConverter.toEntity(categoryDto);
        // set the menu
        categoryForSave.setMenu(menu.get());
        // set the parent category
        categoryForSave.setParentCategory((parentCategory != null) ? parentCategory.get() : null);
        // set category id to all translations
        for(CategoryTranslation translation: categoryForSave.getTranslations()) {
            translation.setCategory(categoryForSave);
        }
        // save category
        Category savedCategory = categoryRepository.save(categoryForSave);

        // return category converted category
        return categoryConverter.toDto(savedCategory);
    }

    @Override
    public List<CategoryOutputDto> getAll(Long id) throws Exception {
        // get menu from db
        Optional<Menu> menu = menuRepository.findById(id);
        if (menu.isEmpty()) {
            throw new Exception("Selected menu doesn't exist!");
        }
        return categoryRepository
                .findCategoriesByMenu(menu.get())
                .stream()
                .map(categoryConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryOutputDto> getAll(Long id, Pageable pageable) throws Exception {
        Optional<Menu> menu = menuRepository.findById(id);
        if (menu.isEmpty()) {
            throw new Exception("Selected menu doesn't exist!");
        }
        return categoryRepository
                .findCategoriesByMenu(menu.get(), pageable)
                .map(categoryConverter::toDto);
    }

    @Override
    public CategoryOutputDto getById(Long id) throws Exception {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new Exception("Category doesn't exist!");
        }
        return categoryConverter.toDto(category.get());
    }

    @Override
    public CategoryOutputDto update(Long id, CategoryInputDto categoryDto) throws Exception {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new Exception("Category doesn't exist!");
        }

        Optional<Category> parentCategory = null;
        if (categoryDto.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(categoryDto.getParentCategoryId());
            if (parentCategory.isEmpty()) {
                throw new Exception("Selected parent category doesn't exist!");
            }
        }

        Category categoryForUpdate = categoryConverter.toEntity(categoryDto);
        categoryForUpdate.setId(id);
        categoryForUpdate.setMenu(category.get().getMenu());
        categoryForUpdate.setParentCategory((parentCategory != null) ? parentCategory.get() : null);
        updateCategoryTranslations(category.get(), categoryForUpdate);

        Category updatedCategory = categoryRepository.save(categoryForUpdate);

        return categoryConverter.toDto(updatedCategory);
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new Exception("Category doesn't exist!");
        }
        categoryRepository.delete(category.get());
    }


    private void updateCategoryTranslations(Category dbCategory, Category categoryForUpdate) {

        Map<String, CategoryTranslation> dbTranslationsMap = new HashMap<>();
        dbCategory.getTranslations().forEach(translation -> dbTranslationsMap.put(translation.getTranslation(), translation));

        for(CategoryTranslation translation: categoryForUpdate.getTranslations()) {
            translation.setCategory(categoryForUpdate);

            CategoryTranslation dbTranslation = dbTranslationsMap.get(translation.getTranslation());
            if(dbTranslation != null) {
                translation.setId(dbTranslation.getId());
                dbTranslationsMap.remove(translation.getTranslation());
            }
        }

        if(!dbTranslationsMap.isEmpty()) {

            Iterator<CategoryTranslation> iterator = dbCategory.getTranslations().iterator();

            while (iterator.hasNext()) {
                CategoryTranslation translation = iterator.next();

                if(dbTranslationsMap.containsKey(translation.getTranslation())) {
                    translation.setCategory(null);
                }
            }
        }
    }
}
