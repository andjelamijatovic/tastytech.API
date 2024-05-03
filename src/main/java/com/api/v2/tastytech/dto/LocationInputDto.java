package com.api.v2.tastytech.dto;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Objects;

public class LocationInputDto implements Serializable {

    @NotEmpty(message = "Location name is required field")
    private String name;
    private String description;
    private Double longitude;
    private Double latitude;
    private String phone;
    private String address;
    @NotEmpty(message = "Brand is required")
    private Long brandId;

    public LocationInputDto() {
    }

    public LocationInputDto(String name, String description, Double longitude, Double latitude,
                            String phone, String address, Long brandId) {
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phone = phone;
        this.address = address;
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        if (o == null || getClass() != o.getClass()) return false;
        LocationInputDto that = (LocationInputDto) o;
        return Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(longitude, that.longitude)
                && Objects.equals(latitude, that.latitude)
                && Objects.equals(phone, that.phone)
                && Objects.equals(address, that.address)
                && Objects.equals(brandId, that.brandId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, longitude, latitude, phone, address, brandId);
    }
}
