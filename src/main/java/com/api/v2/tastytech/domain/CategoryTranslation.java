package com.api.v2.tastytech.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "category_translation")
public class CategoryTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String translation;
    private String description;
    @ManyToOne()
    @JoinColumn(name = "language_id")
    private Language language;
    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    public CategoryTranslation() {
    }

    public CategoryTranslation(Long id, Date createdAt, Date updatedAt, String translation,
                               String description, Language language, Category category) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.translation = translation;
        this.description = description;
        this.language = language;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryTranslation that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt)
                && Objects.equals(translation, that.translation)
                && Objects.equals(description, that.description)
                && Objects.equals(language, that.language)
                && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, translation, description, language, category);
    }
}
