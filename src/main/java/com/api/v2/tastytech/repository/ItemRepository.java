package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Category;
import com.api.v2.tastytech.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findItemsByCategory(Category category);
}
