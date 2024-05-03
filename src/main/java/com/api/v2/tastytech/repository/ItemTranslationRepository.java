package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.ItemTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTranslationRepository extends JpaRepository<ItemTranslation, Long> {
}
