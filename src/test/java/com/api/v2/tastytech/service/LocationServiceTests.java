package com.api.v2.tastytech.service;

import com.api.v2.tastytech.converter.impl.LocationConverter;
import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.domain.Location;
import com.api.v2.tastytech.dto.BrandOutputDto;
import com.api.v2.tastytech.dto.LocationInputDto;
import com.api.v2.tastytech.dto.LocationOutputDto;
import com.api.v2.tastytech.repository.BrandRepository;
import com.api.v2.tastytech.repository.LocationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

@SpringBootTest
public class LocationServiceTests {

    @Autowired
    private LocationService locationService;

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private BrandRepository brandRepository;

    @MockBean
    private LocationConverter locationConverter;

    @Test
    public void saveLocationSuccessfullyTest() throws Exception {
        LocationInputDto inputLocation = new LocationInputDto("local x087", "new location", 44.3,
                34.5, "022/822-222", "Address Line 2", 2l);
        Brand brand = new Brand(2l, "m&n", new Date(), new Date());
        Location convertedLocation = new Location( null, new Date(), new Date(), "local x087", "new location",
                44.3, 34.5, "022/822-222", "Address Line 2",
                new Brand(2l, null, null, null));
        Location location = new Location( 7l, new Date(), new Date(), "local x087", "new location",
                44.3, 34.5, "022/822-222", "Address Line 2", brand);
        LocationOutputDto outputLocation = new LocationOutputDto( 7l, "local x087", "new location",
                44.3, 34.5, "022/822-222", "Address Line 2",
                new BrandOutputDto(2l, "m&n"));

        Mockito.when(locationConverter.toDto(location)).thenReturn(outputLocation);
        Mockito.when(locationRepository.save(convertedLocation)).thenReturn(location);
        Mockito.when(locationConverter.toEntity(inputLocation)).thenReturn(convertedLocation);
        Mockito.when(brandRepository.findById(inputLocation.getBrandId())).thenReturn(Optional.of(brand));

        LocationOutputDto savedLocation = locationService.save(inputLocation);

        Assertions.assertNotNull(savedLocation);
        Assertions.assertEquals(outputLocation, savedLocation);
    }

    @Test
    public void saveLocationFailureTest() {
        LocationInputDto inputLocation = new LocationInputDto(
                "local x087",
                "new location",
                44.3,
                34.5,
                "022/822-222",
                "Address Line 2",
                2l);

        Mockito.when(brandRepository.findById(inputLocation.getBrandId())).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            locationService.save(inputLocation);
        });
        Assertions.assertEquals("Selected brand doesn't exist!", ex.getMessage());
    }

    @Test
    public void saveLocationInputDtoIsNullTest() {
        LocationInputDto inputLocation = null;

        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            locationService.save(inputLocation);
        });
        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }

    @Test
    public void findAllLocations() throws Exception {
        Brand brand = new Brand(2l, "My Brand", new Date(), new Date());
        List<Location> locations = Arrays.asList(
                new Location(1l, new Date(), new Date(), "Location 1", "new location",
                        23.4, 44.5, "232-221", "Address Line 1", brand),
                new Location(2l, new Date(), new Date(), "Location 2", "new location",
                        33.4, 45.6, "232-532", "Address Line 2", brand)
        );
        List<LocationOutputDto> locationOutputs = Arrays.asList(
                new LocationOutputDto(1l, "Location 1", "new location",
                        23.4, 44.5, "232-221", "Address Line 1",
                        new BrandOutputDto(2l, "My Brand")),
                new LocationOutputDto(2l, "Location 2", "new location",
                        33.4, 45.6, "232-532", "Address Line 2",
                        new BrandOutputDto(2l, "My Brand"))
        );

        Mockito.when(locationConverter.toDto(ArgumentMatchers.any(Location.class))).thenAnswer(invocation -> {
            Location entity = invocation.getArgument(0);
            return  new LocationOutputDto(entity.getId(), entity.getName(), entity.getDescription(),
                    entity.getLongitude(), entity.getLatitude(), entity.getPhone(), entity.getAddress(),
                    new BrandOutputDto(entity.getBrand().getId(), entity.getBrand().getName()));
        });
        Mockito.when(locationRepository.findLocationByBrand(brand)).thenReturn(locations);
        Mockito.when(brandRepository.findById(2l)).thenReturn(Optional.of(brand));

        List<LocationOutputDto> result = locationService.findAll(2l);

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(locationOutputs.toArray(), result.toArray());
    }

    @Test
    public void findAllEmptyListOfLocationsTest() throws Exception {
        Brand brand = new Brand(2l, "My Brand", new Date(), new Date());

        Mockito.when(locationRepository.findLocationByBrand(brand)).thenReturn(Collections.emptyList());
        Mockito.when(brandRepository.findById(2l)).thenReturn(Optional.of(brand));

        List<LocationOutputDto> result = locationService.findAll(2l);

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(new LocationOutputDto[0], result.toArray());
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void findAllLocationsFailureTest() throws Exception {

        Mockito.when(brandRepository.findById(2l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            locationService.findAll(2l);
        });
        Assertions.assertEquals("Brand doesn't exist!", ex.getMessage());
    }

    @Test
    public void findLocationByIdSuccessfullyTest() throws Exception {
        Brand brand = new Brand(2l, "Coffie shop", new Date(), new Date());
        Location location = new Location(7l, new Date(), new Date(), "local x087", "new location",
                44.3, 34.5, "022/822-222", "Address Line 2", brand);
        LocationOutputDto outputLocation = new LocationOutputDto( 7l, "local x087", "new location",
                44.3, 34.5, "022/822-222", "Address Line 2",
                new BrandOutputDto(2l, "Coffie shop"));

        Mockito.when(locationConverter.toDto(location)).thenReturn(outputLocation);
        Mockito.when(locationRepository.findById(7l)).thenReturn(Optional.of(location));

        LocationOutputDto dbLocation = locationService.findById(7l);

        Assertions.assertNotNull(dbLocation);
        Assertions.assertEquals(outputLocation, dbLocation);
    }

    @Test
    public void findLocationByIdFailureTest() {
        Mockito.when(locationRepository.findById(7l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            locationService.findById(7l);
        });
        Assertions.assertEquals("Location doesn't exist!", ex.getMessage());
    }

    @Test
    public void updateLocationSuccessfullyTest() throws Exception {
        LocationInputDto inputLocation = new LocationInputDto("local x087", "brand new local", 44.3,
                34.5, "022/822-222", "Downtown Street", 2l);
        Brand brand = new Brand(2l, "m&n", new Date(), new Date());
        Location dbLocation = new Location(7l, new Date(), new Date(), "local x087", "new location",
                44.3, 34.5, "022/822-222", "Address Line 2", brand);
        Location convertedLocation = new Location( null, new Date(), new Date(), "local x087", "brand new local",
                44.3, 34.5, "022/822-222", "Downtown Street",
                new Brand(2l, null, null, null));
        Location updatedLocation = new Location( 7l, new Date(), new Date(), "local x087", "brand new local",
                44.3, 34.5, "022/822-222", "Downtown Street", brand);
        LocationOutputDto outputLocation = new LocationOutputDto( 7l, "local x087", "brand new local",
                44.3, 34.5, "022/822-222", "Downtown Street",
                new BrandOutputDto(2l, "m&n"));

        Mockito.when(locationConverter.toDto(updatedLocation)).thenReturn(outputLocation);
        Mockito.when(locationRepository.save(convertedLocation)).thenReturn(updatedLocation);
        Mockito.when(locationConverter.toEntity(inputLocation)).thenReturn(convertedLocation);
        Mockito.when(locationRepository.findById(7l)).thenReturn(Optional.of(dbLocation));

        LocationOutputDto result = locationService.update(7l, inputLocation);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(outputLocation, result);
    }

    @Test
    public void updateLocationFailureTest() throws Exception {
        LocationInputDto inputLocation = new LocationInputDto("local x087", "brand new local", 44.3,
                34.5, "022/822-222", "Downtown Street", 2l);

        Mockito.when(locationRepository.findById(7l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            locationService.update(7l, inputLocation);
        });
        Assertions.assertEquals("Location doesn't exist!", ex.getMessage());
    }

    @Test
    public void updateLocationInputDtoIsNullTest() {
        LocationInputDto inputLocation = null;
        Brand brand = new Brand(2l, "m&n", new Date(), new Date());
        Location dbLocation = new Location(7l, new Date(), new Date(), "local x087", "new location",
                44.3, 34.5, "022/822-222", "Address Line 2", brand);

        Mockito.when(locationRepository.findById(7l)).thenReturn(Optional.of(dbLocation));
        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            locationService.update(7l, inputLocation);
        });
        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }

    @Test
    public void deleteLocationSuccessfullyTest() throws Exception {
        Brand brand = new Brand(2l, "Coffie shop", new Date(), new Date());
        Location dbLocation = new Location(7l, new Date(), new Date(), "local x087", "new location",
                44.3, 34.5, "022/822-222", "Address Line 2", brand);

        Mockito.doNothing().when(locationRepository).delete(dbLocation);
        Mockito.when(locationRepository.findById(7l)).thenReturn(Optional.of(dbLocation));

        locationService.delete(7l);

        Mockito.verify(locationRepository, Mockito.times(1))
                .delete(ArgumentMatchers.any(Location.class));
    }

    @Test
    public void deleteLocationFailureTest() throws Exception {

        Mockito.when(locationRepository.findById(7l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            locationService.delete(7l);
        });
        Assertions.assertEquals("Location doesn't exist!", ex.getMessage());
    }
}
