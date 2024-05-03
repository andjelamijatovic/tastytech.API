package com.api.v2.tastytech.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CategoryOutputDto implements Serializable {

    private Long id;
    private String category;
    private String description;
    private String parentCategory;
    private List<CategoryTranslationOutputDto> translations;
    private List<ItemOutputDto> items;

    public CategoryOutputDto() {
    }

    public CategoryOutputDto(Long id, String category, String description, String parentCategory,
                             List<CategoryTranslationOutputDto> translations, List<ItemOutputDto> items) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.parentCategory = parentCategory;
        this.translations = translations;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<CategoryTranslationOutputDto> getTranslations() {
        return translations;
    }

    public void setTranslations(List<CategoryTranslationOutputDto> translations) {
        this.translations = translations;
    }

    public List<ItemOutputDto> getItems() {
        return items;
    }

    public void setItems(List<ItemOutputDto> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryOutputDto that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(category, that.category)
                && Objects.equals(description, that.description)
                && Objects.equals(parentCategory, that.parentCategory)
                && Objects.equals(translations, that.translations)
                && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, description, parentCategory, translations, items);
    }
}
