package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.CategoryTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryTranslationRepository extends JpaRepository<CategoryTranslation, Long> {
}
