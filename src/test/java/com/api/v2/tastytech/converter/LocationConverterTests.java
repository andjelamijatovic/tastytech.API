package com.api.v2.tastytech.converter;

import com.api.v2.tastytech.converter.impl.BrandConverter;
import com.api.v2.tastytech.converter.impl.LocationConverter;
import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.domain.Location;
import com.api.v2.tastytech.dto.BrandOutputDto;
import com.api.v2.tastytech.dto.LocationInputDto;
import com.api.v2.tastytech.dto.LocationOutputDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class LocationConverterTests {

    @Autowired
    private LocationConverter locationConverter;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @MockBean
    private BrandConverter brandConverter;

    @Test
    public void toDtoTest() {
        Brand brand = new Brand(2l, "Coffie Room", null, null);
        BrandOutputDto brandDto = new BrandOutputDto(2l, "Coffie Room");
        Location location = new Location(
                1l,
                new Date(),
                new Date(),
                "Location 4",
                "Cafe with small garden and nice view",
                34.3,
                33.3,
                "037/222-333",
                "Some street 12, Belgrade",
                brand);
        LocationOutputDto locationDto = new LocationOutputDto(
                1l,
                "Location 4",
                "Cafe with small garden and nice view",
                34.3,
                33.3,
                "037/222-333",
                "Some street 12, Belgrade",
                brandDto);

        Mockito.when(brandConverter.toDto(brand)).thenReturn(brandDto);

        LocationOutputDto locationResult = locationConverter.toDto(location);

        Assertions.assertNotNull(locationResult);
        Assertions.assertEquals(locationDto, locationResult);
    }

    @Test
    public void toDtoEntityBrandIsNull () {
        Location location = new Location(
                1l,
                new Date(),
                new Date(),
                "Location 4",
                "Cafe with small garden and nice view",
                34.3,
                33.3,
                "037/222-333",
                "Some street 12, Belgrade",
                null);

        Mockito.when(brandConverter.toDto(null)).thenThrow(NullPointerException.class);

        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            locationConverter.toDto(location);
        });

        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }

    @Test
    public void toDtoEntityLocationIsNull() {

        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            locationConverter.toDto(null);
        });

        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }

    @Test
    public void toEntity() throws Exception{
        Date formatedDate = sdf.parse(sdf.format(new Date()));

        LocationInputDto locationDto = new LocationInputDto(
                "Location 4",
                "Cafe with small garden and nice view",
                34.3,
                33.3,
                "037/222-333",
                "Some street 12, Belgrade",
                2l);
        Location location = new Location(
                null,
                formatedDate,
                formatedDate,
                "Location 4",
                "Cafe with small garden and nice view",
                34.3,
                33.3,
                "037/222-333",
                "Some street 12, Belgrade",
                new Brand(2l, null, null, null));

        Location locationResult = locationConverter.toEntity(locationDto);

        Assertions.assertNotNull(locationResult);
        Assertions.assertEquals(location, locationResult);
    }

    @Test
    public void toEntityDtoBrandIsNull () {
        LocationInputDto locationDto = new LocationInputDto(
                "Location 4",
                "Cafe with small garden and nice view",
                34.3,
                33.3,
                "037/222-333",
                "Some street 12, Belgrade",
                null);

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            locationConverter.toEntity(locationDto);
        });

        Assertions.assertInstanceOf(Exception.class, ex);
        Assertions.assertEquals("Brand id is required!", ex.getMessage());
    }

    @Test
    public void toEntityDtoLocationIsNull() {

        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            locationConverter.toEntity(null);
        });

        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }
}
