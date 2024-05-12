package com.api.v2.tastytech.service.impl;

import com.api.v2.tastytech.converter.impl.MenuConverter;
import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.domain.Menu;
import com.api.v2.tastytech.dto.CategoryOutputDto;
import com.api.v2.tastytech.dto.MenuInputDto;
import com.api.v2.tastytech.dto.MenuOutputDto;
import com.api.v2.tastytech.repository.BrandRepository;
import com.api.v2.tastytech.repository.MenuRepository;
import com.api.v2.tastytech.service.MenuService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final BrandRepository brandRepository;
    private final MenuConverter menuConverter;

    public MenuServiceImpl(MenuRepository menuRepository, BrandRepository brandRepository, MenuConverter menuConverter) {
        this.menuRepository = menuRepository;
        this.brandRepository = brandRepository;
        this.menuConverter = menuConverter;
    }

    @Override
    public MenuOutputDto save(MenuInputDto menuDto) throws Exception {
        Optional<Brand> brand = brandRepository.findById(menuDto.getBrandId());
        if(brand.isEmpty()) {
            throw new Exception("Selected brand doesn't exist!");
        }
        Menu savedMenu = menuRepository.save(menuConverter.toEntity(menuDto));
        return menuConverter.toDto(savedMenu);
    }

    @Override
    public List<MenuOutputDto> getAll(Long id) throws Exception {
        Optional<Brand> brand = brandRepository.findById(id);
        if(brand.isEmpty()) {
            throw new Exception("Brand doesn't exist!");
        }
        return menuRepository
                .findAllByBrand(brand.get())
                .stream()
                .map(menuConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MenuOutputDto> getAll(Long id, Pageable pageable) throws Exception {
        Optional<Brand> brand = brandRepository.findById(id);
        if(brand.isEmpty()) {
            throw new Exception("Brand doesn't exist!");
        }
        return menuRepository
                .findMenusByBrand(brand.get(), pageable)
                .map(menuConverter::toDto);
    }

    @Override
    public MenuOutputDto getById(Long id) throws Exception {
        Optional<Menu> menu = menuRepository.findById(id);
        if(menu.isEmpty()) {
            throw new Exception("Menu doesn't exist!");
        }
        return menuConverter.toDto(menu.get());
    }

    @Override
    public MenuOutputDto update(Long id, MenuInputDto menuDto) throws Exception {
        Optional<Menu> menu = menuRepository.findById(id);
        if(menu.isEmpty()) {
            throw new Exception("Menu doesn't exist!");
        }
        Menu menuForUpdate = menuConverter.toEntity(menuDto);
        menuForUpdate.setId(id);
        Menu updatedMenu = menuRepository.save(menuForUpdate);
        return menuConverter.toDto(updatedMenu);
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<Menu> menu = menuRepository.findById(id);
        if(menu.isEmpty()) {
            throw new Exception("Menu doesn't exist!");
        }
        try {
            menuRepository.delete(menu.get());
        } catch (DataIntegrityViolationException ex) {
            throw new Exception("Cannot delete menu due to existing references.");
        }
    }
}
