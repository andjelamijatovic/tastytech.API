package com.api.v2.tastytech.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "item_translation")
public class ItemTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String name;
    private String description;
    @ManyToOne()
    @JoinColumn(name = "language_id")
    private Language language;
    @ManyToOne()
    @JoinColumn(name = "item_id")
    private Item item;

    public ItemTranslation() {
    }

    public ItemTranslation(Long id, Date createdAt, Date updatedAt, String name, String description,
                           Language language, Item item) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.description = description;
        this.language = language;
        this.item = item;
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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemTranslation that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(language, that.language)
                && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, name, description, language, item);
    }
}
