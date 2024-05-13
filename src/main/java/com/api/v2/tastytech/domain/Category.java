package com.api.v2.tastytech.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    @ManyToOne()
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;
    @ManyToOne()
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CategoryTranslation> translations;
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Item> items;

    public Category() {
    }

    public Category(Long id, String name, String description, Date createdAt, Date updatedAt, Category parentCategory, Menu menu,
                    List<CategoryTranslation> translations, List<Item> items) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.parentCategory = parentCategory;
        this.menu = menu;
        this.translations = translations;
        this.items = items;
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

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<CategoryTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<CategoryTranslation> translations) {
        this.translations = translations;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return Objects.equals(id, category.id)
                && Objects.equals(name, category.name)
                && Objects.equals(description, category.description)
                && Objects.equals(createdAt, category.createdAt)
                && Objects.equals(updatedAt, category.updatedAt)
                && Objects.equals(parentCategory, category.parentCategory)
                && Objects.equals(menu, category.menu)
                && Objects.equals(translations, category.translations)
                && Objects.equals(items, category.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, createdAt, updatedAt, parentCategory, menu, translations, items);
    }
}
