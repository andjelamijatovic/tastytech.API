package com.api.v2.tastytech.dto;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Objects;

public class BrandInputDto implements Serializable {

    @NotEmpty(message = "Brand name is required field!")
    private String name;

    public BrandInputDto() {
    }

    public BrandInputDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandInputDto that = (BrandInputDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
