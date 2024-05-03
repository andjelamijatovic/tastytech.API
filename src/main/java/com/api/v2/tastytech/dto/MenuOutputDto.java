package com.api.v2.tastytech.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class MenuOutputDto implements Serializable {

    private Long id;
    private String name;
    private BrandOutputDto brand;
    private List<CategoryOutputDto> categories;

    public MenuOutputDto() {
    }

    public MenuOutputDto(Long id, String name, BrandOutputDto brand, List<CategoryOutputDto> categories) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BrandOutputDto getBrand() {
        return brand;
    }

    public void setBrand(BrandOutputDto brand) {
        this.brand = brand;
    }

    public List<CategoryOutputDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryOutputDto> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuOutputDto that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(brand, that.brand)
                && Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, brand, categories);
    }

}
