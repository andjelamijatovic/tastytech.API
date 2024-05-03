package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Brand;
import com.api.v2.tastytech.domain.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class MenuRepositoryTests {

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private MenuRepository menuRepository;

    @Test
    public void findAllByBrandTest() {
        Brand brand = brandRepository.save(new Brand(33l, "test brand", new Date(), new Date()));
        Assertions.assertNotNull(brand);

        List<Menu> menus = new ArrayList<>();
        Menu menu1 = menuRepository.save(new Menu(22l, new Date(), new Date(), "menu-1", brand, null));
        Assertions.assertNotNull(menu1);
        menus.add(menu1);

        Menu menu2 = menuRepository.save(new Menu(23l, new Date(), new Date(), "menu-2", brand, null));
        Assertions.assertNotNull(menu2);
        menus.add(menu2);

        List<Menu> result = menuRepository.findAllByBrand(brand);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(menus.size(), result.size());
        Assertions.assertEquals(menu1.getName(), result.get(0).getName());
        Assertions.assertEquals(menu2.getName(), result.get(1).getName());
    }
}
