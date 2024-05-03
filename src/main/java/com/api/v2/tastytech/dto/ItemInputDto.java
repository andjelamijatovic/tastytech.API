package com.api.v2.tastytech.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ItemInputDto implements Serializable {

    private Double price;
    private List<ItemTranslationInputDto> translations;

    public ItemInputDto() {
    }

    public ItemInputDto(Double price, List<ItemTranslationInputDto> translations) {
        this.price = price;
        this.translations = translations;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<ItemTranslationInputDto> getTranslations() {
        return translations;
    }

    public void setTranslations(List<ItemTranslationInputDto> translations) {
        this.translations = translations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemInputDto that)) return false;
        return Objects.equals(price, that.price)
                && Objects.equals(translations, that.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, translations);
    }
}
