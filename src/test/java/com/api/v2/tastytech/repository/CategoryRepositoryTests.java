package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.domain.Category;
import com.api.v2.tastytech.domain.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private MenuRepository menuRepository;


    @Test
    public void findCategoriesByMenuTest() {
        Brand brand = brandRepository.save(new Brand(11l, "Mini Brand", new Date(), new Date()));
        Assertions.assertNotNull(brand);

        Menu menu = menuRepository.save(new Menu(56l, new Date(), new Date(), "Kids menu", brand, null));
        Assertions.assertNotNull(menu);

        List<Category> categories = new ArrayList<>();
        Category drinkCategory = categoryRepository.save(new Category(1l, "drink", "", new Date(), new Date(), null, menu, null, null));
        Assertions.assertNotNull(drinkCategory);
        categories.add(drinkCategory);
        Category softDrinkCategory = categoryRepository.save(new Category(2l, "soft drink", "", new Date(), new Date(), drinkCategory, menu, null, null));
        Assertions.assertNotNull(softDrinkCategory);
        categories.add(softDrinkCategory);

        List<Category> result = categoryRepository.findCategoriesByMenu(menu);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(categories.size(), result.size());
        Assertions.assertEquals("drink", result.get(0).getName());
        Assertions.assertEquals("soft drink", result.get(1).getName());
        Assertions.assertEquals("drink", result.get(1).getParentCategory().getName());
    }
}
