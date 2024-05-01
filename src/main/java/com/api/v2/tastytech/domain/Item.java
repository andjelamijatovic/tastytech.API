package com.api.v2.tastytech.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu_item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Double price;
    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<ItemTranslation> translations;

    public Item() {
    }

    public Item(Long id, Date createdAt, Date updatedAt, Double price, Category category,
                List<ItemTranslation> translations) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.price = price;
        this.category = category;
        this.translations = translations;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<ItemTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<ItemTranslation> translations) {
        this.translations = translations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(id, item.id)
                && Objects.equals(createdAt, item.createdAt)
                && Objects.equals(updatedAt, item.updatedAt)
                && Objects.equals(price, item.price)
                && Objects.equals(category, item.category)
                && Objects.equals(translations, item.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, price, category, translations);
    }
}
