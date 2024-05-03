package com.api.v2.tastytech.dto;

import java.io.Serializable;
import java.util.Objects;

public class MenuInputDto implements Serializable {

    private String name;
    private Long brandId;

    public MenuInputDto() {
    }

    public MenuInputDto(String name, Long brandId) {
        this.name = name;
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuInputDto that)) return false;
        return Objects.equals(name, that.name)
                && Objects.equals(brandId, that.brandId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, brandId);
    }
}
