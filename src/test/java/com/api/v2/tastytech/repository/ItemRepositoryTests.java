package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Category;
import com.api.v2.tastytech.domain.Item;
import com.api.v2.tastytech.domain.ItemTranslation;
import com.api.v2.tastytech.domain.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class ItemRepositoryTests {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void findItemsByCategorySuccessfullyTest() {
        Category category = categoryRepository.save(new Category(2l, "soft drinks", "0% alcohol", new Date(), new Date(),
                null, null, null, null));
        List<Item> items = Arrays.asList(
                new Item(34l, new Date(), new Date(), 4.50, category, Arrays.asList(
                        new ItemTranslation(null, new Date(), new Date(), "lemonade", "fresh summer drink",
                                new Language(1l, "english", "en_EN"), null),
                        new ItemTranslation(null, new Date(), new Date(), "limunada", "",
                                new Language(1l, "serbian", "sr_SR"), null)
                )),
                new Item(36l, new Date(), new Date(), 9.10, category, Arrays.asList(
                        new ItemTranslation(null, new Date(), new Date(), "orange juice", "",
                                new Language(1l, "english", "en_EN"), null),
                        new ItemTranslation(null, new Date(), new Date(), "sok od pomorandze", "",
                                new Language(1l, "serbian", "sr_SR"), null)
                ))
        );
    }
}
