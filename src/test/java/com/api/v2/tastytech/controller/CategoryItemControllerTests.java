package com.api.v2.tastytech.controller;

import com.api.v2.tastytech.dto.ItemInputDto;
import com.api.v2.tastytech.dto.ItemOutputDto;
import com.api.v2.tastytech.dto.ItemTranslationInputDto;
import com.api.v2.tastytech.dto.ItemTranslationOutputDto;
import com.api.v2.tastytech.service.ItemService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(CategoryItemController.class)
public class CategoryItemControllerTests {

    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;

    @Test
    public void createNewCategoryItemSuccessfullyTest() throws Exception {
        ItemInputDto itemInput = new ItemInputDto(4.50, Arrays.asList(
                new ItemTranslationInputDto("lemonade", "fresh summer drink", "en_US"),
                new ItemTranslationInputDto("limunada", "osvezavajuci napitak", "sr_SR")
        ));
        ItemOutputDto itemOutput = new ItemOutputDto(34l, 4.50, Arrays.asList(
                new ItemTranslationOutputDto("lemonade", "fresh summer drink", "english", "en_EN"),
                new ItemTranslationOutputDto("limunada", "osvezavajuci napitak", "serbian", "sr_SR")
        ));

        Mockito.when(itemService.save(45l, itemInput)).thenReturn(itemOutput);

        mockMvc.perform(MockMvcRequestBuilders.post("/category/{categoryId}/item", 45l)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objMapper.writeValueAsString(itemInput)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(itemOutput.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.equalTo(itemOutput.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[0].item", CoreMatchers.equalTo("lemonade")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[0].description", CoreMatchers.equalTo("fresh summer drink")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[0].language", CoreMatchers.equalTo("english")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[0].culturalCode", CoreMatchers.equalTo("en_EN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[1].item", CoreMatchers.equalTo("limunada")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[1].description", CoreMatchers.equalTo("osvezavajuci napitak")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[1].language", CoreMatchers.equalTo("serbian")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[1].culturalCode", CoreMatchers.equalTo("sr_SR")));

        Mockito.verify(itemService, Mockito.times(1)).save(45l, itemInput);
    }

    @Test
    public void saveCategoryItemCategoryDoesntExistTest() throws Exception {
        ItemInputDto itemInput = new ItemInputDto(4.50, Arrays.asList(
                new ItemTranslationInputDto("lemonade", "fresh summer drink", "en_US"),
                new ItemTranslationInputDto("limunada", "osvezavajuci napitak", "sr_SR")
        ));

        Mockito.doThrow(new Exception("Selected category doesn't exist!")).when(itemService).save(45l, itemInput);

        var res = mockMvc.perform(MockMvcRequestBuilders.post("/category/{categoryId}/item", 45l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(itemInput)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"message\":\"Selected category doesn't exist!\"}", res);
    }

    @Test
    public void getAllItemsSuccessfullyTest() throws Exception {
        List<ItemOutputDto> itemsDto = Arrays.asList(
                new ItemOutputDto(34l, 4.50, Arrays.asList(
                        new ItemTranslationOutputDto("lemonade", "fresh summer drink", "english", "en_EN"),
                        new ItemTranslationOutputDto("limunada", "", "serbian", "sr_SR")
                )),
                new ItemOutputDto(36l, 9.10, Arrays.asList(
                        new ItemTranslationOutputDto("orange juice", "", "english", "en_EN"),
                        new ItemTranslationOutputDto("sok od pomorandze", "", "serbian", "sr_SR")
                ))
        );

        Mockito.when(itemService.getAll(3l)).thenReturn(itemsDto);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/category/{categoryId}/item", 3l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<ItemOutputDto> receivedItems = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<List<ItemOutputDto>>() {
                });
        Assertions.assertArrayEquals(itemsDto.toArray(), receivedItems.toArray());
    }

    @Test
    public void getAllItemsCategoryDoesntExistTest() throws Exception {

        Mockito.doThrow(new Exception("Selected category doesn't exist!")).when(itemService).getAll(2l);

        mockMvc.perform(MockMvcRequestBuilders.get("/category/{categoryId}/item", 2l))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllItemsEmptyListTest() throws Exception {
        List<ItemOutputDto> itemsDto = new ArrayList<>();

        Mockito.when(itemService.getAll(2l)).thenReturn(itemsDto);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/category/{categoryId}/item", 2l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<ItemOutputDto> receivedItems = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<List<ItemOutputDto>>() {
                });
        Assertions.assertTrue(receivedItems.isEmpty());
    }

    @Test
    public void getAllItemsPageableSuccessfullyTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        List<ItemOutputDto> itemsDto = Arrays.asList(
                new ItemOutputDto(34l, 4.50, Arrays.asList(
                        new ItemTranslationOutputDto("lemonade", "fresh summer drink", "english", "en_EN"),
                        new ItemTranslationOutputDto("limunada", "", "serbian", "sr_SR")
                )),
                new ItemOutputDto(36l, 9.10, Arrays.asList(
                        new ItemTranslationOutputDto("orange juice", "", "english", "en_EN"),
                        new ItemTranslationOutputDto("sok od pomorandze", "", "serbian", "sr_SR")
                )),
                new ItemOutputDto(37l, 2.10, Arrays.asList(
                        new ItemTranslationOutputDto("coffee", "", "english", "en_EN"),
                        new ItemTranslationOutputDto("kafa", "", "serbian", "sr_SR")
                ))
        );
        Page<ItemOutputDto> itemsPerPage = new PageImpl<>(itemsDto, pageable, itemsDto.size());

        Mockito.when(itemService.getAll(3l, pageable)).thenReturn(itemsPerPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/category/{categoryId}/item/paging", 3l)
                        .param("page", "0")
                        .param("pageSize", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(itemsPerPage)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(itemsPerPage.getContent().size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(itemsPerPage.getTotalElements()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(itemsPerPage.getTotalPages()));
    }

    @Test
    public void getAllItemsPageableEmptyListTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        List<ItemOutputDto> itemsDto = new ArrayList<>();
        Page<ItemOutputDto> itemsPerPage = new PageImpl<>(itemsDto, pageable, itemsDto.size());

        Mockito.when(itemService.getAll(3l, pageable)).thenReturn(itemsPerPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/category/{categoryId}/item/paging", 3l)
                        .param("page", "0")
                        .param("pageSize", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(itemsPerPage)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(itemsPerPage.getContent().size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(itemsPerPage.getTotalElements()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(itemsPerPage.getTotalPages()));
    }

    @Test
    public void getAllItemsPageableCategoryDoesntExistTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);

        Mockito.doThrow(new Exception("Selected category doesn't exist!")).when(itemService).getAll(2l, pageable);

        mockMvc.perform(MockMvcRequestBuilders.get("/category/{categoryId}/item/paging", 2l))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getItemByIdSuccessfullyTest() throws Exception {
        ItemOutputDto itemDto = new ItemOutputDto(34l, 4.50, Arrays.asList(
                new ItemTranslationOutputDto("lemonade", "fresh summer drink", "english", "en_EN"),
                new ItemTranslationOutputDto("limunada", "osvezavajuci napitak", "serbian", "sr_SR")
        ));

        Mockito.when(itemService.getById(34l)).thenReturn(itemDto);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/category/{categoryId}/item/{id}", 5l, 34l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        ItemOutputDto receivedItem = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<ItemOutputDto>() {
                });

        Assertions.assertEquals(receivedItem, itemDto);
    }

    @Test
    public void getItemByIdNullReturnedTest() throws Exception {

        Mockito.when(itemService.getById(34l)).thenReturn(null);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/category/{categoryId}/item/{id}", 5l, 34l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = res.getResponse().getContentAsString();

        Assertions.assertTrue(content.isEmpty() || content.isBlank() || content.equals("null"));
    }

    @Test
    public void getItemByIdFailureTest() throws Exception {

        Mockito.when(itemService.getById(1l)).thenThrow(Exception.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/category/{categoryId}/item/{id}", 5l, 1l))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateItemSuccessfullyTest() throws Exception {
        ItemInputDto itemInput = new ItemInputDto(4.50, Arrays.asList(
                new ItemTranslationInputDto("lemonade", "fresh summer drink", "en_US"),
                new ItemTranslationInputDto("limunada", "osvezavajuci napitak", "sr_SR")
        ));
        ItemOutputDto itemOutput = new ItemOutputDto(34l, 4.50, Arrays.asList(
                new ItemTranslationOutputDto("lemonade", "fresh summer drink", "english", "en_EN"),
                new ItemTranslationOutputDto("limunada", "osvezavajuci napitak", "serbian", "sr_SR")
        ));

        Mockito.when(itemService.update(34l, itemInput)).thenReturn(itemOutput);

        mockMvc.perform(MockMvcRequestBuilders.put("/category/{categoryId}/item/{id}", 30l, 34l)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objMapper.writeValueAsString(itemInput)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(itemOutput.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.equalTo(itemOutput.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[0].item", CoreMatchers.equalTo("lemonade")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[0].description", CoreMatchers.equalTo("fresh summer drink")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[0].language", CoreMatchers.equalTo("english")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[0].culturalCode", CoreMatchers.equalTo("en_EN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[1].item", CoreMatchers.equalTo("limunada")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[1].description", CoreMatchers.equalTo("osvezavajuci napitak")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[1].language", CoreMatchers.equalTo("serbian")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.translations[1].culturalCode", CoreMatchers.equalTo("sr_SR")));

        Mockito.verify(itemService, Mockito.times(1)).update(34l, itemInput);
    }

    @Test
    public void updateItemItemDoesntExistTest() throws Exception {
        ItemInputDto itemInput = new ItemInputDto(4.50, Arrays.asList(
                new ItemTranslationInputDto("lemonade", "fresh summer drink", "en_US"),
                new ItemTranslationInputDto("limunada", "osvezavajuci napitak", "sr_SR")
        ));

        Mockito.doThrow(new Exception("Item doesn't exist!")).when(itemService).update(45l, itemInput);

        var res = mockMvc.perform(MockMvcRequestBuilders.put("/category/{categoryId}/item/{id}", 30l, 45l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(itemInput)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"message\":\"Item doesn't exist!\"}", res);
    }

    @Test
    public void deleteItemSuccessfullyTest() throws Exception {
        Mockito.doNothing().when(itemService).delete(65l);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.delete("/category/{categoryId}/item/{id}", 22l, 65l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = res.getResponse().getContentAsString();

        Assertions.assertEquals("Item successfully removed!", content);
    }

    @Test
    public void deleteItemFailureTest() throws Exception {
        Mockito.doThrow(new Exception("Item doesn't exist!")).when(itemService).delete(1l);

        var res = mockMvc.perform(MockMvcRequestBuilders.delete("/category/{categoryId}/item/{id}", 22l, 1l))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"message\":\"Item doesn't exist!\"}", res);
    }
}
