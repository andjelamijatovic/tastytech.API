package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Optional<Language> findLanguageByCulturalCode(String culturalCode);
}
