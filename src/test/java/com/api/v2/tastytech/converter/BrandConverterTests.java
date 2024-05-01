package com.api.v2.tastytech.converter;

import com.api.v2.tastytech.converter.impl.BrandConverter;
import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.dto.BrandInputDto;
import com.api.v2.tastytech.dto.BrandOutputDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class BrandConverterTests {

    @Autowired
    private BrandConverter brandConverter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void toDtoTest() {
        Brand brand = new Brand(1l, "My Caffe", new Date(), new Date());
        BrandOutputDto brandDto = new BrandOutputDto(1l, "My Caffe");

        BrandOutputDto resultBrand = brandConverter.toDto(brand);

        Assertions.assertNotNull(resultBrand);
        Assertions.assertEquals(brandDto, resultBrand);
    }

    @Test
    public void toDtoEntityBrandIsNullTest() {
        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            brandConverter.toDto(null);
        });

        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }

    @Test
    public void toEntityTest() throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));

        BrandInputDto brandDto = new BrandInputDto("My Caffe");
        Brand brand = new Brand(null, "My Caffe", formatedDate, formatedDate);

        Brand resultBrand = brandConverter.toEntity(brandDto);

        Assertions.assertNotNull(resultBrand);
        Assertions.assertEquals(brand, resultBrand);
    }

    @Test
    public void toEntityBrandDtoIsNullTest() {
        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            brandConverter.toEntity(null);
        });

        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }
}
