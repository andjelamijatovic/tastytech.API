package com.api.v2.tastytech.controller;

import com.api.v2.tastytech.dto.CategoryInputDto;
import com.api.v2.tastytech.dto.CategoryOutputDto;
import com.api.v2.tastytech.dto.MenuInputDto;
import com.api.v2.tastytech.dto.MenuOutputDto;
import com.api.v2.tastytech.service.CategoryService;
import com.api.v2.tastytech.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuCategoryController {

    private final MenuService menuService;
    private final CategoryService categoryService;

    public MenuCategoryController(MenuService menuService, CategoryService categoryService) {
        this.menuService = menuService;
        this.categoryService = categoryService;
    }

    @Operation(summary = "CREATE new menu")
    @PostMapping
    public ResponseEntity<MenuOutputDto> saveNewMenu(@RequestBody MenuInputDto menuDto) throws Exception {
        MenuOutputDto menu = menuService.save(menuDto);
        return new ResponseEntity<>(menu, HttpStatus.CREATED);
    }

    @Operation(summary = "GET all menus by brand's id")
    @GetMapping("/brand/{id}")
    public ResponseEntity<List<MenuOutputDto>> findAll(@PathVariable("id") Long id) throws Exception{
        List<MenuOutputDto> menus = menuService.getAll(id);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @Operation(summary = "GET all menus by brand's id - PAGEABLE")
    @GetMapping("/brand/{id}/paging")
    public ResponseEntity<Page<MenuOutputDto>> findAllMenusByPage(
            @PathVariable("id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "1") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection
    ) throws Exception {
        Pageable pageable;
        if (sortDirection.equals("asc")) {
            pageable = PageRequest.of(page, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, pageSize, Sort.by(sortBy).descending());
        }
        Page<MenuOutputDto> menuPerPage = menuService.getAll(id, pageable);
        return new ResponseEntity<>(menuPerPage, HttpStatus.OK);
    }

    @Operation(summary = "GET menu by its id")
    @GetMapping("/{id}")
    public ResponseEntity<MenuOutputDto> findById(@PathVariable("id") Long id) throws Exception{
        MenuOutputDto menu = menuService.getById(id);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @Operation(summary = "UPDATE menu")
    @PutMapping("/{id}")
    public ResponseEntity<MenuOutputDto> update(@PathVariable("id") Long id, @RequestBody MenuInputDto menuDto) throws Exception {
        MenuOutputDto menu = menuService.update(id, menuDto);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @Operation(summary = "DELETE menu by its id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) throws Exception {
        menuService.delete(id);
        return new ResponseEntity<>("Menu successfully removed!", HttpStatus.OK);
    }

    //POST: menu/{id}/category
    //GET: menu/{id}/category
    //GET: menu/{id}/category/{id}
    //PUT: menu/{id}/category/{id}
    //DELETE: menu/{id}/category/{id}
    @Operation(summary = "CREATE new menu category")
    @PostMapping("/{menuId}/category")
    public ResponseEntity<CategoryOutputDto> save(@PathVariable("menuId") Long id, @RequestBody CategoryInputDto categoryDto) throws Exception {
        CategoryOutputDto category = categoryService.save(id, categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @Operation(summary = "GET all categories by menu's id")
    @GetMapping("/{menuId}/category")
    public ResponseEntity<List<CategoryOutputDto>> findAllCategories(@PathVariable("menuId") Long id) throws Exception {
        List<CategoryOutputDto> categories = categoryService.getAll(id);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(summary = "GET all categories by menu's id - PAGEABLE")
    @GetMapping("/{menuId}/paging")
    public ResponseEntity<Page<CategoryOutputDto>> findAllCategoriesByPage(
            @PathVariable("menuId") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "2") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection
    ) throws Exception {
        Pageable pageable;
        if (sortDirection.equals("asc")) {
            pageable = PageRequest.of(page, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, pageSize, Sort.by(sortBy).descending());
        }
        Page<CategoryOutputDto> categoryPerPage = categoryService.getAll(id, pageable);
        return new ResponseEntity<>(categoryPerPage, HttpStatus.OK);
    }

    @Operation(summary = "GET category by its id")
    @GetMapping("/{menuId}/category/{id}")
    public ResponseEntity<CategoryOutputDto> findCategoryById(
            @PathVariable("menuId") Long menuId,
            @PathVariable("id") Long id) throws Exception {
        CategoryOutputDto category = categoryService.getById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @Operation(summary = "UPDATE menu category")
    @PutMapping("/{menuId}/category/{id}")
    public ResponseEntity<CategoryOutputDto> updateCategory(
            @PathVariable("menuId") Long menuId,
            @PathVariable("id") Long id,
            @RequestBody CategoryInputDto categoryDto) throws Exception {
        CategoryOutputDto category = categoryService.update(id, categoryDto);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @Operation(summary = "DELETE category by its id")
    @DeleteMapping("/{menuId}/category/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable("menuId") Long menuId,
            @PathVariable("id") Long id) throws Exception {
        categoryService.delete(id);
        return new ResponseEntity<>("Category successfully removed!", HttpStatus.OK);
    }
}
