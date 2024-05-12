package com.api.v2.tastytech.controller;

import com.api.v2.tastytech.dto.ItemInputDto;
import com.api.v2.tastytech.dto.ItemOutputDto;
import com.api.v2.tastytech.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryItemController {

    private final ItemService itemService;

    public CategoryItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    //POST: category/{id}/item
    @Operation(summary = "CREATE new category item")
    @PostMapping("/{categoryId}/item")
    public ResponseEntity<ItemOutputDto> save(@PathVariable("categoryId") Long id, @RequestBody ItemInputDto itemDto) throws Exception {
        ItemOutputDto item = itemService.save(id, itemDto);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    //GET: category/{id}/item
    @Operation(summary = "GET all items by category's id")
    @GetMapping("/{categoryId}/item")
    public ResponseEntity<List<ItemOutputDto>> findAllItems(@PathVariable("categoryId") Long id) throws Exception {
        List<ItemOutputDto> items = itemService.getAll(id);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    //GET: category/{id}/paging
    @Operation(summary = "GET all items by category's id - PAGEABLE")
    @GetMapping("/{categoryId}/paging")
    public ResponseEntity<Page<ItemOutputDto>> findAllItemsByPage(
            @PathVariable("categoryId") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "2") int pageSize
    ) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ItemOutputDto> itemPerPage = itemService.getAll(id, pageable);
        return new ResponseEntity<>(itemPerPage, HttpStatus.OK);
    }

    //GET: category/{id}/item/{id}
    @Operation(summary = "GET item by its id")
    @GetMapping("/{categoryId}/item/{id}")
    public ResponseEntity<ItemOutputDto> findCategoryById(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("id") Long id) throws Exception {
        ItemOutputDto item = itemService.getById(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    //PUT: category/{id}/item/{id}
    @Operation(summary = "UPDATE category item")
    @PutMapping("/{categoryId}/item/{id}")
    public ResponseEntity<ItemOutputDto> update(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("id") Long id,
            ItemInputDto itemDto) throws Exception {
        ItemOutputDto item = itemService.update(id, itemDto);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    //DELETE: category/{id}/item/{id}
    @Operation(summary = "DELETE item by its id")
    @DeleteMapping("/{categoryId}/item/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("id") Long id) throws Exception {
        itemService.delete(id);
        return new ResponseEntity<>("Item successfully removed!", HttpStatus.OK);
    }
}
