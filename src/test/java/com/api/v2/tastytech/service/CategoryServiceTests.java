package com.api.v2.tastytech.service;

import com.api.v2.tastytech.converter.impl.CategoryConverter;
import com.api.v2.tastytech.domain.*;
import com.api.v2.tastytech.dto.CategoryInputDto;
import com.api.v2.tastytech.dto.CategoryOutputDto;
import com.api.v2.tastytech.dto.CategoryTranslationInputDto;
import com.api.v2.tastytech.dto.CategoryTranslationOutputDto;
import com.api.v2.tastytech.repository.CategoryRepository;
import com.api.v2.tastytech.repository.CategoryTranslationRepository;
import com.api.v2.tastytech.repository.LanguageRepository;
import com.api.v2.tastytech.repository.MenuRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
public class CategoryServiceTests {

    @Autowired
    private CategoryService categoryService;
    @MockBean
    private MenuRepository menuRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private LanguageRepository languageRepository;
    @MockBean
    private CategoryTranslationRepository categoryTranslationRepository;
    @MockBean
    private CategoryConverter categoryConverter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void saveCategorySuccessfullyTest() throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));
        Menu menu = new Menu(1l, new Date(), new Date(), "My Menu",
                new Brand(1l, "Brand New", new Date(), new Date()), null);
        CategoryInputDto categoryDto = new CategoryInputDto("soft drinks", "", 1l, Arrays.asList(
                new CategoryTranslationInputDto("soft drinks", "", "en_EN"),
                new CategoryTranslationInputDto("bezalkoholna pica", "", "sr_SR")
        ));
        Category parentCategory = new Category(1l, "drinks", "", new Date(), new Date(), null, menu, null, null);
        Category categoryForSave = new Category(null, "soft drinks", "", formatedDate, formatedDate, null, null,
                Arrays.asList(
                        new CategoryTranslation(null, formatedDate, formatedDate, "soft drinks", "", null, null),
                        new CategoryTranslation(null, formatedDate, formatedDate, "bezalkoholna pica", "", null, null)
                ), null);
        Category savedCategory = new Category(7l, "soft drinks", "", formatedDate, formatedDate,
                parentCategory, menu, null, null);
        CategoryOutputDto finalCategoryDto = new CategoryOutputDto(7l, "soft drinks", "", "drinks", Arrays.asList(
                new CategoryTranslationOutputDto("soft drinks", "", "english", "en_EN"),
                new CategoryTranslationOutputDto("bezalkoholna pica", "", "serbian", "sr_SR")
        ), null);

        Mockito.when(categoryConverter.toDto(savedCategory)).thenReturn(finalCategoryDto);
        Mockito.when(categoryTranslationRepository.save(ArgumentMatchers.any(CategoryTranslation.class))).thenAnswer(invocationOnMock -> {
            CategoryTranslation translation = invocationOnMock.getArgument(0);
            return new CategoryTranslation(translation.getId(), translation.getCreatedAt(), translation.getUpdatedAt(), translation.getTranslation(),
                    translation.getDescription(), translation.getLanguage(), translation.getCategory());
        });
        Mockito.when(languageRepository.findLanguageByCulturalCode(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(new Language(1l, "english", "en_EN")))
                .thenReturn(Optional.of(new Language(2l, "serbian", "sr_SR")));
        Mockito.when(categoryRepository.save(categoryForSave)).thenReturn(savedCategory);
        Mockito.when(categoryConverter.toEntity(categoryDto)).thenReturn(categoryForSave);
        Mockito.when(categoryRepository.findById(categoryDto.getParentCategoryId())).thenReturn(Optional.of(parentCategory));
        Mockito.when(menuRepository.findById(1l)).thenReturn(Optional.of(menu));

        CategoryOutputDto result = categoryService.save(1l, categoryDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(finalCategoryDto, result);
    }

    @Test
    public void saveCategoryCantFindMenuTest() {

        CategoryInputDto categoryDto = new CategoryInputDto("soft drinks", "", 1l, Arrays.asList(
                new CategoryTranslationInputDto("soft drinks", "", "en_EN"),
                new CategoryTranslationInputDto("bezalkoholna pica", "", "sr_SR")
        ));

        Mockito.when(menuRepository.findById(1l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            categoryService.save(1l, categoryDto);
        });
        Assertions.assertEquals("Selected menu doesn't exist!", ex.getMessage());
    }

    @Test
    public void saveCategoryFindParentCategoryTest() {

        Menu menu = new Menu(1l, new Date(), new Date(), "My Menu",
                new Brand(1l, "Brand New", new Date(), new Date()), null);
        CategoryInputDto categoryDto = new CategoryInputDto("soft drinks", "", 1l, Arrays.asList(
                new CategoryTranslationInputDto("soft drinks", "", "en_EN"),
                new CategoryTranslationInputDto("bezalkoholna pica", "", "sr_SR")
        ));

        Mockito.when(categoryRepository.findById(categoryDto.getParentCategoryId())).thenReturn(Optional.empty());
        Mockito.when(menuRepository.findById(1l)).thenReturn(Optional.of(menu));

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            categoryService.save(1l, categoryDto);
        });
        Assertions.assertEquals("Selected parent category doesn't exist!", ex.getMessage());
    }

    @Test
    public void saveCategoryUnknownLanguageTest() throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));
        Menu menu = new Menu(1l, new Date(), new Date(), "My Menu",
                new Brand(1l, "Brand New", new Date(), new Date()), null);
        CategoryInputDto categoryDto = new CategoryInputDto("soft drinks", "", 1l, Arrays.asList(
                new CategoryTranslationInputDto("soft drinks", "", "en_EN"),
                new CategoryTranslationInputDto("bezalkoholna pica", "", "sr_SR")
        ));
        Category parentCategory = new Category(1l, "drinks", "", new Date(), new Date(), null, menu, null, null);
        Category categoryForSave = new Category(null, "soft drinks", "", formatedDate, formatedDate, null, null,
                Arrays.asList(
                        new CategoryTranslation(null, formatedDate, formatedDate, "soft drinks", "", null, null),
                        new CategoryTranslation(null, formatedDate, formatedDate, "bezalkoholna pica", "", null, null)
                ), null);
        Category savedCategory = new Category(7l, "soft drinks", "", formatedDate, formatedDate,
                parentCategory, menu, null, null);

        Mockito.when(languageRepository.findLanguageByCulturalCode(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.save(categoryForSave)).thenReturn(savedCategory);
        Mockito.when(categoryConverter.toEntity(categoryDto)).thenReturn(categoryForSave);
        Mockito.when(categoryRepository.findById(categoryDto.getParentCategoryId())).thenReturn(Optional.of(parentCategory));
        Mockito.when(menuRepository.findById(1l)).thenReturn(Optional.of(menu));

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            categoryService.save(1l, categoryDto);
        });
        Assertions.assertEquals("Unknown language!", ex.getMessage());
    }

    @Test
    public void getAllCategoriesSuccessfullyTest() throws Exception {
        Menu menu = new Menu(1l, new Date(), new Date(), "My Menu",
                new Brand(1l, "Brand New", new Date(), new Date()), null);
        Category parentCategory = new Category(1l, "drinks", "", new Date(), new Date(), null, menu, null, null);
        Language english = new Language(1l, "english", "en_EN");
        Language serbian = new Language(2l, "serbian", "sr_SR");
        List<Category> categories = Arrays.asList(
                new Category(7l, "soft drinks", "", new Date(), new Date(), parentCategory, menu,
                        Arrays.asList(
                                new CategoryTranslation(45l, new Date(), new Date(), "soft drinks", "", english, null),
                                new CategoryTranslation(56l, new Date(), new Date(), "bezalkoholna pica", "", serbian, null)
                        ), null),
                new Category(8l, "coffee", "", new Date(), new Date(), parentCategory, menu,
                        Arrays.asList(
                                new CategoryTranslation(77l, new Date(), new Date(), "coffee", "", english, null),
                                new CategoryTranslation(78l, new Date(), new Date(), "kafe", "", serbian, null)
                        ), null)
        );
        List<CategoryOutputDto> categoriesDto = Arrays.asList(
                new CategoryOutputDto(7l, "soft drinks", "", "drinks", Arrays.asList(
                        new CategoryTranslationOutputDto("soft drinks", "", "english", "en_EN"),
                        new CategoryTranslationOutputDto("bezalkoholna pica", "", "serbian", "sr_SR")
                ), null),
                new CategoryOutputDto(8l, "coffee", "", "drinks", Arrays.asList(
                        new CategoryTranslationOutputDto("coffee", "", "english", "en_EN"),
                        new CategoryTranslationOutputDto("kafe", "", "serbian", "sr_SR")
                ), null)
        );

        Mockito.when(categoryConverter.toDto(ArgumentMatchers.any(Category.class))).thenAnswer(invocation -> {
            Category entity = invocation.getArgument(0);
            List<CategoryTranslationOutputDto> dtoList = entity.getTranslations()
                    .stream()
                    .map(translation -> {
                        return new CategoryTranslationOutputDto(translation.getTranslation(), translation.getDescription(),
                                translation.getLanguage().getLanguage(), translation.getLanguage().getCulturalCode());
                    })
                    .toList();
            return new CategoryOutputDto(entity.getId(), entity.getName(), entity.getDescription(), entity.getParentCategory().getName(), dtoList, null);
        });
        Mockito.when(categoryRepository.findCategoriesByMenu(menu)).thenReturn(categories);
        Mockito.when(menuRepository.findById(1l)).thenReturn(Optional.of(menu));

        List<CategoryOutputDto> result = categoryService.getAll(1l);

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(categoriesDto.toArray(), result.toArray());
    }

    @Test
    public void getAllCategoriesCantFindMenuTest() {
        Mockito.when(menuRepository.findById(1l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            categoryService.getAll(1l);
        });
        Assertions.assertEquals("Selected menu doesn't exist!", ex.getMessage());
    }

    @Test
    public void getAllEmptyListOfCategoriesTest() throws Exception {
        Menu menu = new Menu(1l, new Date(), new Date(), "My Menu",
                new Brand(1l, "Brand New", new Date(), new Date()), null);

        Mockito.when(categoryRepository.findCategoriesByMenu(menu)).thenReturn(Collections.emptyList());
        Mockito.when(menuRepository.findById(1l)).thenReturn(Optional.of(menu));

        List<CategoryOutputDto> result = categoryService.getAll(1l);

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(new CategoryOutputDto[0], result.toArray());
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void getCategoryByIdSuccessfullyTest() throws Exception {
        Category category = new Category(1l, "drinks", "", new Date(), new Date(), null, null,
                Arrays.asList(
                        new CategoryTranslation(44l, new Date(), new Date(), "drinks", "",
                                new Language(1l, "english", "en_EN"), null),
                        new CategoryTranslation(45l, new Date(), new Date(), "pice", "",
                                new Language(2l, "serbian", "sr_SR"), null)
                ), null);
        CategoryOutputDto categoryDto = new CategoryOutputDto(1l, "drinks", "", null,
                Arrays.asList(
                        new CategoryTranslationOutputDto("drinks", "", "english", "en_EN"),
                        new CategoryTranslationOutputDto("pice", "", "serbian", "sr_SR")
                ), null);

        Mockito.when(categoryConverter.toDto(category)).thenReturn(categoryDto);
        Mockito.when(categoryRepository.findById(1l)).thenReturn(Optional.of(category));

        CategoryOutputDto result = categoryService.getById(1l);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(categoryDto, result);
    }

    @Test
    public void getCategoryByIdFailureTest() {
        Mockito.when(categoryRepository.findById(1l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            categoryService.getById(1l);
        });
        Assertions.assertEquals("Category doesn't exist!", ex.getMessage());
    }

    @Test
    public void deleteCategoryByIdSuccessfullyTest() throws Exception {
        Category category = new Category(1l, "drinks", "", new Date(), new Date(), null, null,
                Arrays.asList(
                        new CategoryTranslation(44l, new Date(), new Date(), "drinks", "",
                                new Language(1l, "english", "en_EN"), null),
                        new CategoryTranslation(45l, new Date(), new Date(), "pice", "",
                                new Language(2l, "serbian", "sr_SR"), null)
                ), null);

        Mockito.doNothing().when(categoryRepository).delete(category);
        Mockito.when(categoryRepository.findById(1l)).thenReturn(Optional.of(category));

        categoryService.delete(1l);

        Mockito.verify(categoryRepository, Mockito.times(1))
                .delete(ArgumentMatchers.any(Category.class));
    }

    @Test
    public void deleteItemFailureTest() {
        Mockito.when(categoryRepository.findById(1l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            categoryService.delete(1l);
        });
        Assertions.assertEquals("Category doesn't exist!", ex.getMessage());
    }
}
