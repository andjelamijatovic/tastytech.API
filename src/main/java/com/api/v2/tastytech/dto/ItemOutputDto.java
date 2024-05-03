package com.api.v2.tastytech.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ItemOutputDto implements Serializable {

    private Long id;
    private Double price;
    private List<ItemTranslationOutputDto> translations;

    public ItemOutputDto() {
    }

    public ItemOutputDto(Long id, Double price, List<ItemTranslationOutputDto> translations) {
        this.id = id;
        this.price = price;
        this.translations = translations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<ItemTranslationOutputDto> getTranslations() {
        return translations;
    }

    public void setTranslations(List<ItemTranslationOutputDto> translations) {
        this.translations = translations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemOutputDto that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(price, that.price)
                && Objects.equals(translations, that.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, translations);
    }
}
