package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Optional<Brand> findBrandByName(String name);

}
