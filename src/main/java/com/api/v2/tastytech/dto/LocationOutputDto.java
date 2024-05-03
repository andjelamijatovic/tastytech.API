package com.api.v2.tastytech.dto;

import java.io.Serializable;
import java.util.Objects;

public class LocationOutputDto implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Double longitude;
    private Double latitude;
    private String phone;
    private String address;
    private BrandOutputDto brand;

    public LocationOutputDto() {
    }

    public LocationOutputDto(Long id, String name, String description, Double longitude,
                             Double latitude, String phone, String address, BrandOutputDto brand) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phone = phone;
        this.address = address;
        this.brand = brand;
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

    public BrandOutputDto getBrand() {
        return brand;
    }

    public void setBrand(BrandOutputDto brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationOutputDto that = (LocationOutputDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(longitude, that.longitude)
                && Objects.equals(latitude, that.latitude)
                && Objects.equals(phone, that.phone)
                && Objects.equals(address, that.address)
                && Objects.equals(brand, that.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, longitude, latitude, phone, address, brand);
    }
}
