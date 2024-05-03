package com.api.v2.tastytech.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CategoryInputDto implements Serializable {

    private String name;
    private String description;
    private Long parentCategoryId;
    private List<CategoryTranslationInputDto> translations;

    public CategoryInputDto() {
    }

    public CategoryInputDto(String name, String description, Long parentCategoryId, List<CategoryTranslationInputDto> translations) {
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.translations = translations;
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

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public List<CategoryTranslationInputDto> getTranslations() {
        return translations;
    }

    public void setTranslations(List<CategoryTranslationInputDto> translations) {
        this.translations = translations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryInputDto that)) return false;
        return Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(parentCategoryId, that.parentCategoryId)
                && Objects.equals(translations, that.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, parentCategoryId, translations);
    }
}
