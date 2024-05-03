package com.api.v2.tastytech.dto;

import java.io.Serializable;
import java.util.Objects;

public class ItemTranslationOutputDto implements Serializable {

    private String item;
    private String description;
    private String language;
    private String culturalCode;

    public ItemTranslationOutputDto() {
    }

    public ItemTranslationOutputDto(String item, String description, String language, String culturalCode) {
        this.item = item;
        this.description = description;
        this.language = language;
        this.culturalCode = culturalCode;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCulturalCode() {
        return culturalCode;
    }

    public void setCulturalCode(String culturalCode) {
        this.culturalCode = culturalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemTranslationOutputDto that)) return false;
        return Objects.equals(item, that.item)
                && Objects.equals(description, that.description)
                && Objects.equals(language, that.language)
                && Objects.equals(culturalCode, that.culturalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, description, language, culturalCode);
    }
}
