package com.api.v2.tastytech.controller;

import com.api.v2.tastytech.dto.LocationInputDto;
import com.api.v2.tastytech.dto.LocationOutputDto;
import com.api.v2.tastytech.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(summary = "CREATE NEW location")
    @PostMapping
    public ResponseEntity<LocationOutputDto> save(@RequestBody LocationInputDto locationDto) throws Exception {
        LocationOutputDto location = locationService.save(locationDto);
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    @Operation(summary = "GET ALL locations by brand's ID")
    @GetMapping("/brand/{id}")
    public ResponseEntity<List<LocationOutputDto>> findAll(@PathVariable("id") Long id) throws Exception{
        List<LocationOutputDto> locations = locationService.findAll(id);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @Operation(summary = "GET location by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<LocationOutputDto> findById(@PathVariable("id") Long id) throws Exception{
        LocationOutputDto location = locationService.findById(id);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @Operation(summary = "UPDATE location")
    @PutMapping("/{id}")
    public ResponseEntity<LocationOutputDto> update(@PathVariable("id") Long id, @RequestBody LocationInputDto locationDto) throws Exception {
        LocationOutputDto location = locationService.update(id, locationDto);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @Operation(summary = "DELET location by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) throws Exception {
        locationService.delete(id);
        return new ResponseEntity<>("Location successfully removed!", HttpStatus.OK);
    }
}
