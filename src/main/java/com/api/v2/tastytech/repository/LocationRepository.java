package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findLocationByBrand(Brand brand);
}
