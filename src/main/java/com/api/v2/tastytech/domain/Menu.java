package com.api.v2.tastytech.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String name;
    @ManyToOne()
    @JoinColumn(name = "brand_id")
    private Brand brand;
    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER)
    private List<Category> categories;

    public Menu() {
    }

    public Menu(Long id, Date createdAt, Date updatedAt, String name, Brand brand, List<Category> categories) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.brand = brand;
        this.categories = categories;
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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu menu)) return false;
        return Objects.equals(id, menu.id)
                && Objects.equals(createdAt, menu.createdAt)
                && Objects.equals(updatedAt, menu.updatedAt)
                && Objects.equals(name, menu.name)
                && Objects.equals(brand, menu.brand)
                && Objects.equals(categories, menu.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, name, brand, categories);
    }
}
