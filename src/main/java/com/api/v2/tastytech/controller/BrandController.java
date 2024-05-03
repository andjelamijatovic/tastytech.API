package com.api.v2.tastytech.controller;

import com.api.v2.tastytech.dto.BrandInputDto;
import com.api.v2.tastytech.dto.BrandOutputDto;
import com.api.v2.tastytech.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @Operation(summary = "CREATE new brand")
    @PostMapping
    public ResponseEntity<BrandOutputDto> save(@Valid @RequestBody BrandInputDto brandDto) throws Exception {
        BrandOutputDto brand = brandService.save(brandDto);
        return new ResponseEntity<>(brand, HttpStatus.CREATED);
    }

    @Operation(summary = "GET ALL brands")
    @GetMapping
    public ResponseEntity<List<BrandOutputDto>> getAll() {
        List<BrandOutputDto> brands = brandService.findAll();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @Operation(summary = "GET brand by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<BrandOutputDto> getById(@PathVariable Long id) throws Exception {
        BrandOutputDto brand = brandService.findById(id);
        return new ResponseEntity<>(brand, HttpStatus.OK);
    }

    @Operation(summary = "UPDATE brand")
    @PutMapping("/{id}")
    public ResponseEntity<BrandOutputDto> update(@PathVariable Long id, @Valid @RequestBody BrandInputDto brandDto) throws Exception {
        BrandOutputDto brand = brandService.update(id, brandDto);
        return new ResponseEntity<>(brand, HttpStatus.OK);
    }

    @Operation(summary = "DELETE brand by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        brandService.delete(id);
        return new ResponseEntity<>("Brand successfully removed!", HttpStatus.OK);
    }

}
