package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.domain.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class LocationRepositoryTests {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void findByBrandTest() {
        Brand brand = brandRepository.save(new Brand(56l, "C&K", new Date(), new Date()));
        Assertions.assertNotNull(brand);

        List<Location> locations = new ArrayList<>();
        Location location = locationRepository.save(new Location(
                1l,
                new Date(),
                new Date(),
                "new location",
                "cookie shop",
                45.5,
                44.5,
                "009/222-111",
                "Betty Street 33, London",
                brand));
        locations.add(location);
        Assertions.assertNotNull(location);

        List<Location> dbLocations = locationRepository.findLocationByBrand(brand);
        Assertions.assertFalse(dbLocations.isEmpty());
        Assertions.assertEquals(locations, dbLocations);
    }
}
