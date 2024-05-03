package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Category;
import com.api.v2.tastytech.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoriesByMenu(Menu menu);
}
