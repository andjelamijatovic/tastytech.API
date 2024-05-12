package com.api.v2.tastytech.service.impl;

import com.api.v2.tastytech.converter.impl.BrandConverter;
import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.dto.BrandInputDto;
import com.api.v2.tastytech.dto.BrandOutputDto;
import com.api.v2.tastytech.repository.BrandRepository;
import com.api.v2.tastytech.service.BrandService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandConverter brandConverter;

    public BrandServiceImpl(BrandRepository brandRepository, BrandConverter brandConverter) {
        this.brandRepository = brandRepository;
        this.brandConverter = brandConverter;
    }

    @Override
    public BrandOutputDto save(BrandInputDto brandDto) throws Exception {
        Optional<Brand> dbBrand = brandRepository.findBrandByName(brandDto.getName());
        if(dbBrand.isPresent()) {
            throw new Exception("Brand already exists!");
        }
        Brand savedBrand = brandRepository.save(brandConverter.toEntity(brandDto));
        return brandConverter.toDto(savedBrand);
    }

    @Override
    public List<BrandOutputDto> findAll() {
        return brandRepository
                .findAll()
                .stream()
                .map(brandConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BrandOutputDto findById(Long id) throws Exception {
        Optional<Brand> dbBrand = brandRepository.findById(id);
        if(dbBrand.isEmpty()) {
            throw new Exception("Brand doesn't exist!");
        }
        return brandConverter.toDto(dbBrand.get());
    }

    @Override
    public BrandOutputDto update(Long id, BrandInputDto brandDto) throws Exception {
        Optional<Brand> dbBrand = brandRepository.findById(id);
        if(dbBrand.isEmpty()) {
            throw new Exception("Brand doesn't exist!");
        }
        Brand brandForUpdate = brandConverter.toEntity(brandDto);
        brandForUpdate.setId(id);
        brandForUpdate.setUpdatedAt(new Date());

        Brand updatedBrand = brandRepository.save(brandForUpdate);
        return brandConverter.toDto(updatedBrand);
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<Brand> dbBrand = brandRepository.findById(id);
        if(dbBrand.isEmpty()) {
            throw new Exception("Brand doesn't exist!");
        }
        try {
            brandRepository.delete(dbBrand.get());
        } catch (DataIntegrityViolationException ex) {
            throw new Exception("Cannot delete brand due to existing references.");
        }
    }

}
