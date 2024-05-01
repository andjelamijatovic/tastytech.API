package com.api.v2.tastytech.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String language;
    private String culturalCode;

    public Language() {
    }

    public Language(Long id, String language, String culturalCode) {
        this.id = id;
        this.language = language;
        this.culturalCode = culturalCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(o instanceof Language language1)) return false;
        return Objects.equals(id, language1.id)
                && Objects.equals(language, language1.language)
                && Objects.equals(culturalCode, language1.culturalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, language, culturalCode);
    }
}
