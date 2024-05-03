package com.api.v2.tastytech.service.impl;

import com.api.v2.tastytech.converter.impl.LocationConverter;
import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.domain.Location;
import com.api.v2.tastytech.dto.LocationInputDto;
import com.api.v2.tastytech.dto.LocationOutputDto;
import com.api.v2.tastytech.repository.BrandRepository;
import com.api.v2.tastytech.repository.LocationRepository;
import com.api.v2.tastytech.service.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationConverter locationConverter;
    private final BrandRepository brandRepository;

    public LocationServiceImpl(LocationRepository locationRepository,
                               LocationConverter locationConverter, BrandRepository brandRepository) {
        this.locationRepository = locationRepository;
        this.locationConverter = locationConverter;
        this.brandRepository = brandRepository;
    }

    @Override
    public LocationOutputDto save(LocationInputDto locationDto) throws Exception {

        Optional<Brand> brand = brandRepository.findById(locationDto.getBrandId());

        if(brand.isEmpty()) {
            throw new Exception("Selected brand doesn't exist!");
        }

        Location savedLocation = locationRepository.save(locationConverter.toEntity(locationDto));
        return locationConverter.toDto(savedLocation);
    }

    @Override
    public List<LocationOutputDto> findAll(Long brandId) throws Exception {

        Optional<Brand> brand = brandRepository.findById(brandId);

        if(brand.isEmpty()) {
            throw new Exception("Brand doesn't exist!");
        }

        return locationRepository
                .findLocationByBrand(brand.get())
                .stream()
                .map(locationConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LocationOutputDto findById(Long id) throws Exception {

        Optional<Location> location = locationRepository.findById(id);

        if(location.isEmpty()) {
            throw new Exception("Location doesn't exist!");
        }

        return locationConverter.toDto(location.get());
    }

    @Override
    public LocationOutputDto update(Long id, LocationInputDto locationDto) throws Exception {

        Optional<Location> location = locationRepository.findById(id);

        if(location.isEmpty()) {
            throw new Exception("Location doesn't exist!");
        }

        Location locationForUpdate = locationConverter.toEntity(locationDto);
        locationForUpdate.setId(id);
//        locationForUpdate.setBrand(location.get().getBrand());
        Location updatedLocation = locationRepository.save(locationForUpdate);

        return locationConverter.toDto(updatedLocation);
    }

    @Override
    public void delete(Long id) throws Exception {

        Optional<Location> location = locationRepository.findById(id);

        if(location.isEmpty()) {
            throw new Exception("Location doesn't exist!");
        }

        locationRepository.delete(location.get());
    }
}
