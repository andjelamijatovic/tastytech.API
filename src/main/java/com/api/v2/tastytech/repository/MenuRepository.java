package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
