package com.api.v2.tastytech.dto;

import java.io.Serializable;
import java.util.Objects;

public class CategoryTranslationOutputDto implements Serializable {

    private String translation;
    private String description;
    private String language;
    private String culturalCode;

    public CategoryTranslationOutputDto() {
    }

    public CategoryTranslationOutputDto(String translation, String description, String language, String culturalCode) {
        this.translation = translation;
        this.description = description;
        this.language = language;
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
        if (!(o instanceof CategoryTranslationOutputDto that)) return false;
        return Objects.equals(translation, that.translation)
                && Objects.equals(description, that.description)
                && Objects.equals(language, that.language)
                && Objects.equals(culturalCode, that.culturalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translation, description, language, culturalCode);
    }
}
