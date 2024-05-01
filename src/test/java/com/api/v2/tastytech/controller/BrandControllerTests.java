package com.api.v2.tastytech.controller;

import com.api.v2.tastytech.dto.BrandInputDto;
import com.api.v2.tastytech.dto.BrandOutputDto;
import com.api.v2.tastytech.service.BrandService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
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

@WebMvcTest(BrandControllerTests.class)
public class BrandControllerTests {

    @MockBean
    private BrandService brandService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;

    @Test
    public void saveBrandSuccessfullyTest() throws Exception {
        BrandInputDto inputBrand = new BrandInputDto("Brand New");
        BrandOutputDto outputBrand = new BrandOutputDto(1l, "Brand New");

        Mockito.when(brandService.save(inputBrand)).thenReturn(outputBrand);

        mockMvc.perform(MockMvcRequestBuilders.post("/brand")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objMapper.writeValueAsString(inputBrand)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(outputBrand.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.equalTo(outputBrand.getName())));

        Mockito.verify(brandService, Mockito.times(1)).save(inputBrand);
    }
    @Test
    public void saveBrandFailureTest() throws Exception {
        BrandInputDto inputBrand = new BrandInputDto("Brand New");

        Mockito.when(brandService.save(inputBrand)).thenThrow(Exception.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/brand").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objMapper.writeValueAsString(inputBrand)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void getAllBrandsTest() throws Exception {
        List<BrandOutputDto> brands = new ArrayList<>(Arrays.asList(
                new BrandOutputDto(1l, "Brand No1"),
                new BrandOutputDto(2l, "bServed"),
                new BrandOutputDto(2l, "Brand New")
        ));

        Mockito.when(brandService.findAll()).thenReturn(brands);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/brand"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<BrandOutputDto> receivedBrands = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<List<BrandOutputDto>>() {
                });

        Assertions.assertArrayEquals(brands.toArray(), receivedBrands.toArray());
    }
    @Test
    public void getAllBrandsEmptyListTest() throws Exception {
        List<BrandOutputDto> brands = new ArrayList<>();

        Mockito.when(brandService.findAll()).thenReturn(brands);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/brand"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<BrandOutputDto> receivedBrands = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<List<BrandOutputDto>>() {
                });

        Assertions.assertTrue(receivedBrands.isEmpty());
    }
    @Test
    public void getBrandByIdSuccessfullyTest() throws Exception {
        BrandOutputDto brand = new BrandOutputDto(1l, "Brand No1");

        Mockito.when(brandService.findById(1l)).thenReturn(brand);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/brand/{id}", 1l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        BrandOutputDto receivedBrand = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<BrandOutputDto>() {
                });

        Assertions.assertEquals(receivedBrand, brand);
    }
    @Test
    public void getBrandByIdNullReturnedTest() throws Exception {

        Mockito.when(brandService.findById(1l)).thenReturn(null);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/brand/{id}", 1l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = res.getResponse().getContentAsString();

        Assertions.assertTrue(content.isEmpty() || content.isBlank() || content.equals("null"));
    }
    @Test
    public void getBrandByIdFailureTest() throws Exception {

        Mockito.when(brandService.findById(1l)).thenThrow(Exception.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/brand/{id}", 1l))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void updateBrandSuccessTest() throws Exception {
        BrandInputDto inputBrand = new BrandInputDto("Brand New");
        BrandOutputDto outputBrand = new BrandOutputDto(1l, "Brand New");

        Mockito.when(brandService.update(1l, inputBrand)).thenReturn(outputBrand);

        mockMvc.perform(MockMvcRequestBuilders.put("/brand/{id}", 1l)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objMapper.writeValueAsString(inputBrand)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(outputBrand.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.equalTo(outputBrand.getName())));

        Mockito.verify(brandService, Mockito.times(1)).update(1l, inputBrand);
    }
    @Test
    public void updateBrandFailureTest() throws Exception {
        BrandInputDto inputBrand = new BrandInputDto("Brand New");

        Mockito.when(brandService.update(1l, inputBrand)).thenThrow(Exception.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/brand/{id}", 1l)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objMapper.writeValueAsString(inputBrand)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void deleteBrandSuccessTest() throws Exception {
        Mockito.doNothing().when(brandService).delete(1l);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.delete("/brand/{id}", 1l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = res.getResponse().getContentAsString();

        Assertions.assertEquals("Brand successfully removed!", content);
    }
    @Test
    public void deleteBrandFailureTest() throws Exception {
        Mockito.doThrow(Exception.class).when(brandService).delete(1l);

        mockMvc.perform(MockMvcRequestBuilders.delete("/brand/{id}", 1l))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
