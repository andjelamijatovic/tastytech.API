package com.api.v2.tastytech.service;

import com.api.v2.tastytech.converter.impl.BrandConverter;
import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.dto.BrandInputDto;
import com.api.v2.tastytech.dto.BrandOutputDto;
import com.api.v2.tastytech.repository.BrandRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

@SpringBootTest
public class BrandServiceTests {

    @Autowired
    private BrandService brandService;
    @MockBean
    private BrandRepository brandRepository;
    @MockBean
    private BrandConverter brandConverter;

    @Test
    public void saveBrandSuccessfullyTest() throws Exception {
        BrandInputDto inputBrand = new BrandInputDto("Coffee Shop");
        Brand brand = new Brand(null, "Coffee Shop", new Date(), new Date());
        Brand savedBrand = new Brand(1l, "Coffee Shop", new Date(), new Date());
        BrandOutputDto outputBrand = new BrandOutputDto(1l, "Coffee Shop");

        Mockito.when(brandConverter.toDto(savedBrand)).thenReturn(outputBrand);
        Mockito.when(brandRepository.save(brand)).thenReturn(savedBrand);
        Mockito.when(brandConverter.toEntity(inputBrand)).thenReturn(brand);
        Mockito.when(brandRepository.findBrandByName(inputBrand.getName())).thenReturn(Optional.empty());

        BrandOutputDto result = brandService.save(inputBrand);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(outputBrand, result);
    }

    @Test
    public void saveBrandFailureTest() {
        BrandInputDto inputBrand = new BrandInputDto("Coffee Shop");
        Brand brand = new Brand(1l, "Coffee Shop", new Date(), new Date());

        Mockito.when(brandRepository.findBrandByName(inputBrand.getName())).thenReturn(Optional.of(brand));

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            brandService.save(inputBrand);
        });
        Assertions.assertEquals("Brand already exists!", ex.getMessage());
    }

    @Test
    public void saveBrandInputDtoIsNullTest() {
        BrandInputDto inputBrand = null;

        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            brandService.save(inputBrand);
        });
        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }

    @Test
    public void getAllBrandsTest() {
        List<Brand> brands = Arrays.asList(
                new Brand(1l, "Coffee Shop", new Date(), new Date()),
                new Brand(2l, "Thrill", new Date(), new Date()),
                new Brand(3l, "My Caffe", new Date(), new Date())
        );

        List<BrandOutputDto> brandOutputs = Arrays.asList(
                new BrandOutputDto(1l, "Coffee Shop"),
                new BrandOutputDto(2l, "Thrill"),
                new BrandOutputDto(3l, "My Caffe")
        );

        Mockito.when(brandConverter.toDto(ArgumentMatchers.any(Brand.class))).thenAnswer(invocation -> {
            Brand entity = invocation.getArgument(0);
            return new BrandOutputDto(entity.getId(), entity.getName());
        });
        Mockito.when(brandRepository.findAll()).thenReturn(brands);

        List<BrandOutputDto> result = brandService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(brandOutputs.toArray(), result.toArray());
    }

    @Test
    public void getAllEmptyListOfBrandsTest() {
        Mockito.when(brandRepository.findAll()).thenReturn(Collections.emptyList());

        List<BrandOutputDto> result = brandService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(new BrandOutputDto[0], result.toArray());
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void getBrandByIdSuccessfullyTest() throws Exception {
        Long id = 1l;
        Brand brand = new Brand(1l, "Coffee Shop", new Date(), new Date());
        BrandOutputDto outputBrand = new BrandOutputDto(1l, "Coffee Shop");

        Mockito.when(brandConverter.toDto(brand)).thenReturn(outputBrand);
        Mockito.when(brandRepository.findById(id)).thenReturn(Optional.of(brand));

        BrandOutputDto result = brandService.findById(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(outputBrand, result);
    }

    @Test
    public void getBrandByIdFailureTest() throws Exception {
        Long id = 1l;

        Mockito.when(brandRepository.findById(id)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            brandService.findById(id);
        });
        Assertions.assertEquals("Brand doesn't exist!", ex.getMessage());
    }

    @Test
    public void updateBrandSuccessfullyTest() throws Exception {
        BrandInputDto inputBrand = new BrandInputDto("Pretty Coffee Shop");
        Brand dbBrand = new Brand(2l, "Coffee Shop", new Date(), new Date());
        Brand convertedBrand = new Brand(2l, "Pretty Coffee Shop", new Date(),  new Date());
        Brand updatedBrand = new Brand(2l, "Pretty Coffee Shop", new Date(), new Date());
        BrandOutputDto outputBrand = new BrandOutputDto(2l, "Pretty Coffee Shop");

        Mockito.when(brandConverter.toDto(updatedBrand)).thenReturn(outputBrand);
        Mockito.when(brandRepository.save(convertedBrand)).thenReturn(updatedBrand);
        Mockito.when(brandConverter.toEntity(inputBrand)).thenReturn(convertedBrand);
        Mockito.when(brandRepository.findById(2l)).thenReturn(Optional.of(dbBrand));

        BrandOutputDto result = brandService.update(2l, inputBrand);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(outputBrand, result);
    }

    @Test
    public void updateBrandFailureTest() throws Exception {
        BrandInputDto inputBrand = new BrandInputDto("Pretty Coffee Shop");

        Mockito.when(brandRepository.findById(2l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            brandService.update(2l, inputBrand);
        });
        Assertions.assertEquals("Brand doesn't exist!", ex.getMessage());
    }

    @Test
    public void updateBrandInputDtoIsNullTest() {
        BrandInputDto inputBrand = null;
        Brand dbBrand = new Brand(2l, "Coffee Shop", new Date(), new Date());

        Mockito.when(brandRepository.findById(2l)).thenReturn(Optional.of(dbBrand));
        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            brandService.update(2l, inputBrand);
        });
        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }

    @Test
    public void deleteBrandSuccessfullyTest() throws Exception {
        Brand dbBrand = new Brand(2l, "Coffee Shop", new Date(), new Date());

        Mockito.doNothing().when(brandRepository).delete(dbBrand);
        Mockito.when(brandRepository.findById(2l)).thenReturn(Optional.of(dbBrand));

        brandService.delete(2l);

        Mockito.verify(brandRepository, Mockito.times(1)).delete(ArgumentMatchers.any(Brand.class));
    }

    @Test
    public void deleteBrandFailureTest() throws Exception {
        Mockito.when(brandRepository.findById(2l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            brandService.delete(2l);
        });
        Assertions.assertEquals("Brand doesn't exist!", ex.getMessage());
    }

    @Test
    public void deleteBrandExistAReferenceTest() throws Exception {
        Brand dbBrand = new Brand(2l, "Coffee Shop", new Date(), new Date());

        Mockito.doThrow(DataIntegrityViolationException.class).when(brandRepository).delete(dbBrand);
        Mockito.when(brandRepository.findById(2l)).thenReturn(Optional.of(dbBrand));

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            brandService.delete(2l);
        });
        Assertions.assertEquals("Cannot delete brand due to existing references.", ex.getMessage());
    }
}
