package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Brand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class RepositoryBrandTests {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void findBrandByNameSuccessTest() {
        Brand brand = brandRepository.save(new Brand(1l, "My Caffe", new Date(), new Date()));
        Assertions.assertNotNull(brand);
        Optional<Brand> dbBrand = brandRepository.findBrandByName("My Caffe");
        Assertions.assertTrue(dbBrand.isPresent());
        Assertions.assertEquals("My Caffe", dbBrand.get().getName());
    }

    @Test
    public void cantFindBrandByNameTest() {
        Optional<Brand> dbBrand = brandRepository.findBrandByName("random brand name");
        Assertions.assertTrue(dbBrand.isEmpty());
    }
}
