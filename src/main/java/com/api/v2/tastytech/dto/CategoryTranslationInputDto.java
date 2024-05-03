package com.api.v2.tastytech.dto;

import java.io.Serializable;
import java.util.Objects;

public class CategoryTranslationInputDto implements Serializable {

    private String translation;
    private String description;
    private String culturalCode;

    public CategoryTranslationInputDto() {
    }

    public CategoryTranslationInputDto(String translation, String description, String culturalCode) {
        this.translation = translation;
        this.description = description;
        this.culturalCode = culturalCode;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof CategoryTranslationInputDto that)) return false;
        return Objects.equals(translation, that.translation)
                && Objects.equals(description, that.description)
                && Objects.equals(culturalCode, that.culturalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translation, description, culturalCode);
    }
}
