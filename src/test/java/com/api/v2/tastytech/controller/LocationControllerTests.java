package com.api.v2.tastytech.controller;

import com.api.v2.tastytech.dto.BrandOutputDto;
import com.api.v2.tastytech.dto.LocationInputDto;
import com.api.v2.tastytech.dto.LocationOutputDto;
import com.api.v2.tastytech.service.LocationService;
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

@WebMvcTest(LocationController.class)
public class LocationControllerTests {

    @MockBean
    private LocationService locationService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;

    @Test
    public void saveLocationSuccessfullyTest() throws Exception {
        LocationInputDto inputLocation = new LocationInputDto("local x087", "new location", 44.3,
                34.5, "022/822-222", "Address Line 2", 2l);
        LocationOutputDto outputLocation = new LocationOutputDto(7l, "local x087", "new location",
                44.3, 34.5, "022/822-222", "Address Line 2",
                new BrandOutputDto(2l, "m&n"));

        Mockito.when(locationService.save(inputLocation)).thenReturn(outputLocation);

        mockMvc.perform(MockMvcRequestBuilders.post("/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(inputLocation)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(outputLocation.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.equalTo(outputLocation.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.equalTo(outputLocation.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.longitude", CoreMatchers.equalTo(outputLocation.getLongitude())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.latitude", CoreMatchers.equalTo(outputLocation.getLatitude())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone", CoreMatchers.equalTo(outputLocation.getPhone())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.equalTo(outputLocation.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand.id", CoreMatchers.equalTo(outputLocation.getBrand().getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand.name", CoreMatchers.equalTo(outputLocation.getBrand().getName())));

        Mockito.verify(locationService, Mockito.times(1)).save(inputLocation);
    }

    @Test
    public void saveLocationBrandDoesntExistTest() throws Exception {
        LocationInputDto inputLocation = new LocationInputDto("local x087", "new location", 44.3,
                34.5, "022/822-222", "Address Line 2", 2l);

        Mockito.doThrow(new Exception("Brand doesn't exist!")).when(locationService).save(inputLocation);

        var res = mockMvc.perform(MockMvcRequestBuilders.post("/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(inputLocation)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"message\":\"Brand doesn't exist!\"}", res);

    }

    @Test
    public void getAllLocationsTest() throws Exception {
        List<LocationOutputDto> locationOutputs = Arrays.asList(
                new LocationOutputDto(1l, "Location 1", "new location",
                        23.4, 44.5, "232-221", "Address Line 1",
                        new BrandOutputDto(2l, "My Brand")),
                new LocationOutputDto(2l, "Location 2", "new location",
                        33.4, 45.6, "232-532", "Address Line 2",
                        new BrandOutputDto(2l, "My Brand"))
        );

        Mockito.when(locationService.findAll(2l)).thenReturn(locationOutputs);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/location/brand/{id}", 2l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<LocationOutputDto> receivedLocation = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<List<LocationOutputDto>>() {
                });
        Assertions.assertArrayEquals(locationOutputs.toArray(), receivedLocation.toArray());
    }

    @Test
    public void getAllLocationsEmptyListTest() throws Exception {
        List<LocationOutputDto> locationOutputs = new ArrayList<>();

        Mockito.when(locationService.findAll(2l)).thenReturn(locationOutputs);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/location/brand/{id}", 2l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<LocationOutputDto> receivedMenus = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<List<LocationOutputDto>>() {
                });
        Assertions.assertTrue(receivedMenus.isEmpty());
    }

    @Test
    public void getLocationByIdSuccessfullyTest() throws Exception {
        LocationOutputDto outputLocation = new LocationOutputDto(7l, "local x087", "new location",
                44.3, 34.5, "022/822-222", "Address Line 2",
                new BrandOutputDto(2l, "m&n"));

        Mockito.when(locationService.findById(7l)).thenReturn(outputLocation);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/location/{id}", 7l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        LocationOutputDto receivedLocation = objMapper.readValue(res.getResponse().getContentAsString(),
                new TypeReference<LocationOutputDto>() {
                });

        Assertions.assertEquals(receivedLocation, outputLocation);
    }

    @Test
    public void getLocationByIdNullReturnedTest() throws Exception {

        Mockito.when(locationService.findById(1l)).thenReturn(null);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get("/location/{id}", 1l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = res.getResponse().getContentAsString();

        Assertions.assertTrue(content.isEmpty() || content.isBlank() || content.equals("null"));
    }

    @Test
    public void getLocationByIdFailureTest() throws Exception {

        Mockito.when(locationService.findById(1l)).thenThrow(Exception.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/location/{id}", 1l))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateLocationSuccessTest() throws Exception {
        LocationInputDto inputLocation = new LocationInputDto("local x087", "new location", 44.3,
                34.5, "022/822-222", "Address Line 2", 2l);
        LocationOutputDto outputLocation = new LocationOutputDto(7l, "local x087", "new location",
                44.3, 34.5, "022/822-222", "Address Line 2",
                new BrandOutputDto(2l, "m&n"));

        Mockito.when(locationService.update(7l, inputLocation)).thenReturn(outputLocation);

        mockMvc.perform(MockMvcRequestBuilders.put("/location/{id}", 7l)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objMapper.writeValueAsString(inputLocation)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(outputLocation.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.equalTo(outputLocation.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.equalTo(outputLocation.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.longitude", CoreMatchers.equalTo(outputLocation.getLongitude())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.latitude", CoreMatchers.equalTo(outputLocation.getLatitude())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone", CoreMatchers.equalTo(outputLocation.getPhone())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.equalTo(outputLocation.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand.id", CoreMatchers.equalTo(outputLocation.getBrand().getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand.name", CoreMatchers.equalTo(outputLocation.getBrand().getName())));

        Mockito.verify(locationService, Mockito.times(1)).update(7l, inputLocation);
    }

    @Test
    public void updateLocationDoesntExistTest() throws Exception {
        LocationInputDto inputLocation = new LocationInputDto("local x087", "new location", 44.3,
                34.5, "022/822-222", "Address Line 2", 2l);

        Mockito.doThrow(new Exception("Location doesn't exist!")).when(locationService).update(7l,
                inputLocation);

        var res = mockMvc.perform(MockMvcRequestBuilders.put("/location/{id}", 7l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(inputLocation)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"message\":\"Location doesn't exist!\"}", res);
    }

    @Test
    public void deleteLocationSuccessfullyTest() throws Exception {
        Mockito.doNothing().when(locationService).delete(7l);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.delete("/location/{id}", 7l))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = res.getResponse().getContentAsString();

        Assertions.assertEquals("Location successfully removed!", content);
    }

    @Test
    public void deleteMenuFailureTest() throws Exception {
        Mockito.doThrow(new Exception("Location doesn't exist!")).when(locationService).delete(1l);

        var res = mockMvc.perform(MockMvcRequestBuilders.delete("/location/{id}", 1l))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"message\":\"Location doesn't exist!\"}", res);
    }
}
