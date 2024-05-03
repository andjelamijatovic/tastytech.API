package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
