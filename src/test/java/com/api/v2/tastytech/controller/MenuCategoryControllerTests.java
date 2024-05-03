package com.api.v2.tastytech.controller;

import com.api.v2.tastytech.dto.*;
import com.api.v2.tastytech.service.CategoryService;
import com.api.v2.tastytech.service.MenuService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(MenuCategoryController.class)
public class MenuCategoryControllerTests {

    @MockBean
    private MenuService menuService;
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;

    @Test
    public void createNewMenuSuccessfullyTest() throws Exception {
        MenuInputDto menuInput = new MenuInputDto("new menu", 3l);
        MenuOutputDto menuOutput = new MenuOutputDto(34l, "new menu", new BrandOutputDto(3l, "brand new"), null);

        Mockito.when(menuService.save(menuInput)).thenReturn(menuOutput);

        mockMvc.perform(MockMvcRequestBuilders.post("/menu")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objMapper.writeValueAsString(menuInput)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(menuOutput.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.equalTo(menuOutput.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand.id", CoreMatchers.equalTo(menuOutput.getBrand().getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand.name", CoreMatchers.equalTo(menuOutput.getBrand().getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categories", CoreMatchers.equalTo(menuOutput.getCategories())));

        Mockito.verify(menuService, Mockito.times(1)).save(menuInput);
    }

    @Test
    public void saveMenuBrandDoesntExistTest() throws Exception {
        MenuInputDto menuInput = new MenuInputDto("new menu", 3l);

        Mockito.doThrow(new Exception("Brand doesn't exist!")).when(menuService).save(menuInput);

        var res = mockMvc.perform(MockMvcRequestBuilders.post("/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(menuInput)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"message\":\"Brand doesn't exist!\"}", res);

    }

    @Test
    public void getAllMenusSuccessfullyTest() throws Exception {
        BrandOutputDto brandDto = new BrandOutputDto(2l, "My Brand");
        List<MenuOutputDto> menusDto = Arrays.asList(
                new MenuOutputDto(8l, "menu", brandDto, null),
                new MenuOutputDto(9l, "kids menu", brandDto, null)
        );

        Mockito.when(menuService.getAll(2l)).thenReturn(menusDto);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/menu/brand/{id}", 2l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<MenuOutputDto> receivedMenus = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<List<MenuOutputDto>>() {
                });
        Assertions.assertArrayEquals(menusDto.toArray(), receivedMenus.toArray());
    }

    @Test
    public void getAllMenusEmptyListTest() throws Exception {
        List<MenuOutputDto> menusDto = new ArrayList<>();

        Mockito.when(menuService.getAll(2l)).thenReturn(menusDto);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/menu/brand/{id}", 2l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<MenuOutputDto> receivedMenu = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<List<MenuOutputDto>>() {
                });
        Assertions.assertTrue(receivedMenu.isEmpty());
    }

    @Test
    public void getMenuByIdSuccessfullyTest() throws Exception {
        MenuOutputDto menuDto = new MenuOutputDto(45l, "menu-1", new BrandOutputDto(3l, "my brand"), Arrays.asList(
                new CategoryOutputDto(5l, "drinks", "", null, Arrays.asList(
                        new CategoryTranslationOutputDto("drinks", "", "english", "en_EN"),
                        new CategoryTranslationOutputDto("pice", "", "serbian", "sr_SR")
                ), Arrays.asList(
                        new ItemOutputDto(34l, 4.50, Arrays.asList(
                                new ItemTranslationOutputDto("lemonade", "", "english", "en_EN"),
                                new ItemTranslationOutputDto("limunada", "", "serbian", "sr_SR")
                        )),
                        new ItemOutputDto(35l, 8.90, Arrays.asList(
                                new ItemTranslationOutputDto("orange juice", "", "english", "en_EN"),
                                new ItemTranslationOutputDto("sok od narandze", "", "serbian", "sr_SR")
                        ))
                )),
                new CategoryOutputDto(6l, "food", "", null, Arrays.asList(
                        new CategoryTranslationOutputDto("food", "", "english", "en_EN"),
                        new CategoryTranslationOutputDto("hrana", "", "serbian", "sr_SR")
                ), null)
        ));

        Mockito.when(menuService.getById(45l)).thenReturn(menuDto);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/menu/{id}", 45l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        MenuOutputDto receivedMenu = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<MenuOutputDto>() {
                });

        Assertions.assertEquals(receivedMenu, menuDto);
    }

    @Test
    public void getMenuByIdNullReturnedTest() throws Exception {

        Mockito.when(menuService.getById(1l)).thenReturn(null);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/menu/{id}", 1l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = res.getResponse().getContentAsString();

        Assertions.assertTrue(content.isEmpty() || content.isBlank() || content.equals("null"));
    }

    @Test
    public void getMenuByIdFailureTest() throws Exception {

        Mockito.when(menuService.getById(1l)).thenThrow(Exception.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/menu/{id}", 1l))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateMenuSuccessfullyTest() throws Exception {
        MenuInputDto menuInput = new MenuInputDto("new menu", 3l);
        MenuOutputDto menuOutput = new MenuOutputDto(34l, "new menu", new BrandOutputDto(3l, "brand new"), null);

        Mockito.when(menuService.update(34l, menuInput)).thenReturn(menuOutput);

        mockMvc.perform(MockMvcRequestBuilders.put("/menu/{id}", 34l)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objMapper.writeValueAsString(menuInput)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(menuOutput.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.equalTo(menuOutput.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand.id", CoreMatchers.equalTo(menuOutput.getBrand().getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand.name", CoreMatchers.equalTo(menuOutput.getBrand().getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categories", CoreMatchers.equalTo(menuOutput.getCategories())));

        Mockito.verify(menuService, Mockito.times(1)).update(34l, menuInput);
    }

    @Test
    public void updateMenuDoesntExistTest() throws Exception {
        MenuInputDto menuInput = new MenuInputDto("new menu", 3l);

        Mockito.doThrow(new Exception("Menu doesn't exist!")).when(menuService).update(45l,
                menuInput);

        var res = mockMvc.perform(MockMvcRequestBuilders.put("/menu/{id}", 45l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(menuInput)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"message\":\"Menu doesn't exist!\"}", res);
    }

    @Test
    public void deleteMenuSuccessfullyTest() throws Exception {
        Mockito.doNothing().when(menuService).delete(45l);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.delete("/menu/{id}", 45l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = res.getResponse().getContentAsString();

        Assertions.assertEquals("Menu successfully removed!", content);
    }

    @Test
    public void deleteMenuFailureTest() throws Exception {
        Mockito.doThrow(new Exception("Menu doesn't exist!")).when(menuService).delete(1l);

        var res = mockMvc.perform(MockMvcRequestBuilders.delete("/menu/{id}", 1l))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"message\":\"Menu doesn't exist!\"}", res);
    }

    @Test
    public void createNewMenuCategorySuccessfullyTest() throws Exception {
        CategoryInputDto categoryInput = new CategoryInputDto("soft drinks", "", 1l, Arrays.asList(
                new CategoryTranslationInputDto("soft drinks", "", "en_EN"),
                new CategoryTranslationInputDto("bezalkoholna pica", "", "sr_SR")
        ));
        CategoryOutputDto categoryOutput = new CategoryOutputDto(7l, "soft drinks", "", "drinks", Arrays.asList(
                new CategoryTranslationOutputDto("soft drinks", "", "english", "en_EN"),
                new CategoryTranslationOutputDto("bezalkoholna pica", "", "serbian", "sr_SR")
        ), null);

        Mockito.when(categoryService.save(45l, categoryInput)).thenReturn(categoryOutput);

        mockMvc.perform(MockMvcRequestBuilders.post("/menu/{menuId}/category", 45l)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objMapper.writeValueAsString(categoryInput)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(categoryOutput.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", CoreMatchers.equalTo(categoryOutput.getCategory())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.equalTo(categoryOutput.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parentCategory", CoreMatchers.equalTo(categoryOutput.getParentCategory())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[0].translation", CoreMatchers.equalTo("soft drinks")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[0].description", CoreMatchers.equalTo("")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[0].language", CoreMatchers.equalTo("english")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[0].culturalCode", CoreMatchers.equalTo("en_EN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[1].translation", CoreMatchers.equalTo("bezalkoholna pica")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[1].description", CoreMatchers.equalTo("")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[1].language", CoreMatchers.equalTo("serbian")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[1].culturalCode", CoreMatchers.equalTo("sr_SR")));

        Mockito.verify(categoryService, Mockito.times(1)).save(45l, categoryInput);
    }

    @Test
    public void saveMenuCategoryMenuDoesntExistTest() throws Exception {
        CategoryInputDto categoryInput = new CategoryInputDto("soft drinks", "", 1l, Arrays.asList(
                new CategoryTranslationInputDto("soft drinks", "", "en_EN"),
                new CategoryTranslationInputDto("bezalkoholna pica", "", "sr_SR")
        ));

        Mockito.doThrow(new Exception("Selected menu doesn't exist!")).when(categoryService).save(45l, categoryInput);

        var res = mockMvc.perform(MockMvcRequestBuilders.post("/menu/{menuId}/category", 45l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(categoryInput)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"message\":\"Selected menu doesn't exist!\"}", res);
    }

    @Test
    public void getAllCategoriesSuccessfullyTest() throws Exception {
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

        Mockito.when(categoryService.getAll(3l)).thenReturn(categoriesDto);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/menu/{menuId}/category", 3l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<CategoryOutputDto> receivedCategories = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<List<CategoryOutputDto>>() {
                });
        Assertions.assertArrayEquals(categoriesDto.toArray(), receivedCategories.toArray());
    }

    @Test
    public void getAllCategoriesEmptyListTest() throws Exception {
        List<CategoryOutputDto> categoriesDto = new ArrayList<>();

        Mockito.when(categoryService.getAll(2l)).thenReturn(categoriesDto);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/menu/{menuId}/category", 2l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<CategoryOutputDto> receivedCategories = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<List<CategoryOutputDto>>() {
                });
        Assertions.assertTrue(receivedCategories.isEmpty());
    }

    @Test
    public void getCategoryByIdSuccessfullyTest() throws Exception {
        CategoryOutputDto categoryDto = new CategoryOutputDto(5l, "drinks", "", null, Arrays.asList(
                new CategoryTranslationOutputDto("drinks", "", "english", "en_EN"),
                new CategoryTranslationOutputDto("pice", "", "serbian", "sr_SR")
        ), Arrays.asList(
                new ItemOutputDto(34l, 4.50, Arrays.asList(
                        new ItemTranslationOutputDto("lemonade", "", "english", "en_EN"),
                        new ItemTranslationOutputDto("limunada", "", "serbian", "sr_SR")
                )),
                new ItemOutputDto(35l, 8.90, Arrays.asList(
                        new ItemTranslationOutputDto("orange juice", "", "english", "en_EN"),
                        new ItemTranslationOutputDto("sok od narandze", "", "serbian", "sr_SR")
                ))
        ));

        Mockito.when(categoryService.getById(5l)).thenReturn(categoryDto);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/menu/{menuId}/category/{id}", 45l, 5l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        CategoryOutputDto receivedCategory = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<CategoryOutputDto>() {
                });

        Assertions.assertEquals(receivedCategory, categoryDto);
    }

    @Test
    public void getCategoryByIdNullReturnedTest() throws Exception {

        Mockito.when(categoryService.getById(5l)).thenReturn(null);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/menu/{menuId}/category/{id}", 45l, 5l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = res.getResponse().getContentAsString();

        Assertions.assertTrue(content.isEmpty() || content.isBlank() || content.equals("null"));
    }

    @Test
    public void getCategoryByIdFailureTest() throws Exception {

        Mockito.when(categoryService.getById(1l)).thenThrow(Exception.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/menu/{menuId}/category/{id}", 45l, 1l))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteCategorySuccessfullyTest() throws Exception {
        Mockito.doNothing().when(categoryService).delete(65l);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.delete("/menu/{menuId}/category/{id}", 22l, 65l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = res.getResponse().getContentAsString();

        Assertions.assertEquals("Category successfully removed!", content);
    }

    @Test
    public void deleteCategoryFailureTest() throws Exception {
        Mockito.doThrow(new Exception("Category doesn't exist!")).when(categoryService).delete(1l);

        var res = mockMvc.perform(MockMvcRequestBuilders.delete("/menu/{menuId}/category/{id}", 22l, 1l))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"message\":\"Category doesn't exist!\"}", res);
    }
}
