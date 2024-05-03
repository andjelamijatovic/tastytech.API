package com.api.v2.tastytech.service.impl;

import com.api.v2.tastytech.converter.impl.CategoryConverter;
import com.api.v2.tastytech.domain.Category;
import com.api.v2.tastytech.domain.CategoryTranslation;
import com.api.v2.tastytech.domain.Language;
import com.api.v2.tastytech.domain.Menu;
import com.api.v2.tastytech.dto.CategoryInputDto;
import com.api.v2.tastytech.dto.CategoryOutputDto;
import com.api.v2.tastytech.repository.CategoryRepository;
import com.api.v2.tastytech.repository.CategoryTranslationRepository;
import com.api.v2.tastytech.repository.LanguageRepository;
import com.api.v2.tastytech.repository.MenuRepository;
import com.api.v2.tastytech.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;
    private final LanguageRepository languageRepository;
    private final CategoryConverter categoryConverter;
    private final CategoryTranslationRepository categoryTranslationRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MenuRepository menuRepository, LanguageRepository languageRepository,
                               CategoryConverter categoryConverter, CategoryTranslationRepository categoryTranslationRepository) {
        this.categoryRepository = categoryRepository;
        this.menuRepository = menuRepository;
        this.languageRepository = languageRepository;
        this.categoryConverter = categoryConverter;
        this.categoryTranslationRepository = categoryTranslationRepository;
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
        // save category
        Category savedCategory = categoryRepository.save(categoryForSave);
        // set language and category id to all translations
        if(categoryForSave.getTranslations() != null && !categoryForSave.getTranslations().isEmpty()) {
            this.saveCategoryTranslations(categoryDto, categoryForSave.getTranslations(), savedCategory);
        }
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
    public CategoryOutputDto getById(Long id) throws Exception {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new Exception("Category doesn't exist!");
        }
        return categoryConverter.toDto(category.get());
    }

    @Override
    public CategoryOutputDto update(Long id, CategoryInputDto categoryDto) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new Exception("Category doesn't exist!");
        }
        categoryRepository.delete(category.get());
    }

    private void saveCategoryTranslations(CategoryInputDto categoryDto, List<CategoryTranslation> translations, Category category) throws Exception {
        //category.setTranslations(new ArrayList<>());
        for (int i = 0; i < categoryDto.getTranslations().size(); i++) {
            translations.get(i).setCategory(category);
            Optional<Language> language = languageRepository.findLanguageByCulturalCode(categoryDto
                    .getTranslations().
                    get(i)
                    .getCulturalCode());
            if (language.isEmpty()) {
                throw new Exception("Unknown language!");
            }
            translations.get(i).setLanguage(language.get());
            categoryTranslationRepository.save(translations.get(i));
        }
    }

}
