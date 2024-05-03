package com.api.v2.tastytech.service;

import com.api.v2.tastytech.converter.impl.ItemConverter;
import com.api.v2.tastytech.domain.Category;
import com.api.v2.tastytech.domain.Item;
import com.api.v2.tastytech.domain.ItemTranslation;
import com.api.v2.tastytech.domain.Language;
import com.api.v2.tastytech.dto.ItemInputDto;
import com.api.v2.tastytech.dto.ItemOutputDto;
import com.api.v2.tastytech.dto.ItemTranslationInputDto;
import com.api.v2.tastytech.dto.ItemTranslationOutputDto;
import com.api.v2.tastytech.repository.CategoryRepository;
import com.api.v2.tastytech.repository.ItemRepository;
import com.api.v2.tastytech.repository.ItemTranslationRepository;
import com.api.v2.tastytech.repository.LanguageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
public class ItemServiceTests {

    @Autowired
    private ItemService itemService;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private ItemConverter itemConverter;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private ItemTranslationRepository itemTranslationRepository;
    @MockBean
    private LanguageRepository languageRepository;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void saveItemSuccessfullyTest() throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));

        Category category = new Category(2l, "soft drinks", "0% alcohol", new Date(), new Date(),
                null, null, null, null);
        ItemInputDto itemDto = new ItemInputDto(4.50, Arrays.asList(
                new ItemTranslationInputDto("lemonade", "fresh summer drink", "en_US"),
                new ItemTranslationInputDto("limunada", "osvezavajuci napitak", "sr_SR")
        ));
        Item itemForSave = new Item(null, formatedDate, formatedDate, 4.50, null, Arrays.asList(
                new ItemTranslation(null, formatedDate, formatedDate, "lemonade",
                        "fresh summer drink", null, null),
                new ItemTranslation(null, formatedDate, formatedDate, "limunada",
                        "osvezavajuci napitak", null, null)
        ));
        Item savedItem = new Item(34l, new Date(),new Date(), 4.50, category, null);
        ItemOutputDto finalItemDto = new ItemOutputDto(34l, 4.50, Arrays.asList(
                new ItemTranslationOutputDto("lemonade", "fresh summer drink", "english", "en_EN"),
                new ItemTranslationOutputDto("limunada", "osvezavajuci napitak", "serbian", "sr_SR")
        ));

        Mockito.when(itemConverter.toDto(savedItem)).thenReturn(finalItemDto);
        Mockito.when(itemTranslationRepository.save(ArgumentMatchers.any(ItemTranslation.class))).thenAnswer(invocationOnMock -> {
            ItemTranslation translation = invocationOnMock.getArgument(0);
            return new ItemTranslation(translation.getId(), translation.getCreatedAt(), translation.getUpdatedAt(),
                    translation.getName(), translation.getDescription(), translation.getLanguage(), translation.getItem());
        });
        Mockito.when(languageRepository.findLanguageByCulturalCode(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(new Language(1l, "english", "en_EN")))
                .thenReturn(Optional.of(new Language(2l, "serbian", "sr_SR")));

        Mockito.when(itemRepository.save(itemForSave)).thenReturn(savedItem);
        Mockito.when(itemConverter.toEntity(itemDto)).thenReturn(itemForSave);
        Mockito.when(categoryRepository.findById(2l)).thenReturn(Optional.of(category));

        ItemOutputDto result = itemService.save(2l, itemDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(finalItemDto, result);
    }

    @Test
    public void saveItemCantFindCategoryTest() throws Exception {
        ItemInputDto itemDto = new ItemInputDto(4.50, Arrays.asList(
                new ItemTranslationInputDto("lemonade", "fresh summer drink", "en_US"),
                new ItemTranslationInputDto("limunada", "osvezavajuci napitak", "sr_SR")
        ));

        Mockito.when(categoryRepository.findById(2l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            itemService.save(2l, itemDto);
        });
        Assertions.assertEquals("Selected category doesn't exist!", ex.getMessage());
    }

    @Test
    public void saveItemUnknownLanguageTest() throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));

        Category category = new Category(2l, "soft drinks", "0% alcohol", new Date(), new Date(),
                null, null, null, null);
        ItemInputDto itemDto = new ItemInputDto(4.50, Arrays.asList(
                new ItemTranslationInputDto("lemonade", "fresh summer drink", "en_US"),
                new ItemTranslationInputDto("limunada", "osvezavajuci napitak", "sr_SR")
        ));
        Item itemForSave = new Item(null, formatedDate, formatedDate, 4.50, null, Arrays.asList(
                new ItemTranslation(null, formatedDate, formatedDate, "lemonade",
                        "fresh summer drink", null, null),
                new ItemTranslation(null, formatedDate, formatedDate, "limunada",
                        "osvezavajuci napitak", null, null)
        ));
        Item savedItem = new Item(34l, new Date(),new Date(), 4.50, category, null);

        Mockito.when(languageRepository.findLanguageByCulturalCode(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        Mockito.when(itemRepository.save(itemForSave)).thenReturn(savedItem);
        Mockito.when(itemConverter.toEntity(itemDto)).thenReturn(itemForSave);
        Mockito.when(categoryRepository.findById(2l)).thenReturn(Optional.of(category));

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            itemService.save(2l, itemDto);
        });
        Assertions.assertEquals("Unknown language!", ex.getMessage());
    }

    @Test
    public void saveItemDtoIsNullTest() throws Exception {
        Category category = new Category(2l, "soft drinks", "0% alcohol", new Date(), new Date(),
                null, null, null, null);

        Mockito.when(categoryRepository.findById(2l)).thenReturn(Optional.of(category));

        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            itemService.save(2l, null);
        });
        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }

    @Test
    public void getAllItemsSuccessfullyTest() throws Exception {
        Category category = new Category(2l, "soft drinks", "0% alcohol", new Date(), new Date(),
                null, null, null, null);
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
        List<ItemOutputDto> itemsDto = Arrays.asList(
                new ItemOutputDto(34l, 4.50, Arrays.asList(
                        new ItemTranslationOutputDto("lemonade", "fresh summer drink", "english", "en_EN"),
                        new ItemTranslationOutputDto("limunada", "", "serbian", "sr_SR")
                )),
                new ItemOutputDto(36l, 9.10, Arrays.asList(
                        new ItemTranslationOutputDto("orange juice", "", "english", "en_EN"),
                        new ItemTranslationOutputDto("sok od pomorandze", "", "serbian", "sr_SR")
                ))
        );

        Mockito.when(itemConverter.toDto(ArgumentMatchers.any(Item.class))).thenAnswer(invocation -> {
            Item entity = invocation.getArgument(0);
            List<ItemTranslationOutputDto> dtoList = entity.getTranslations()
                    .stream()
                    .map(translation -> {
                        return new ItemTranslationOutputDto(translation.getName(), translation.getDescription(),
                                translation.getLanguage().getLanguage(), translation.getLanguage().getCulturalCode());
                    })
                    .toList();
            return  new ItemOutputDto(entity.getId(), entity.getPrice(), dtoList);
        });
        Mockito.when(itemRepository.findItemsByCategory(category)).thenReturn(items);
        Mockito.when(categoryRepository.findById(2l)).thenReturn(Optional.of(category));

        List<ItemOutputDto> result = itemService.getAll(2l);

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(itemsDto.toArray(), result.toArray());
    }

    @Test
    public void getAllItemsCantFindCategoryTest() {
        Mockito.when(categoryRepository.findById(2l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            itemService.getAll(2l);
        });
        Assertions.assertEquals("Selected category doesn't exist!", ex.getMessage());
    }

    @Test
    public void getAllEmptyListOfItemsTest() throws Exception {
        Category category = new Category(2l, "soft drinks", "0% alcohol", new Date(), new Date(),
                null, null, null, null);

        Mockito.when(itemRepository.findItemsByCategory(category)).thenReturn(Collections.emptyList());
        Mockito.when(categoryRepository.findById(2l)).thenReturn(Optional.of(category));

        List<ItemOutputDto> result = itemService.getAll(2l);

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(new ItemOutputDto[0], result.toArray());
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void getItemByIdSuccessfullyTest() throws Exception {
        Category category = new Category(2l, "soft drinks", "0% alcohol", new Date(), new Date(),
                null, null, null, null);
        Item item = new Item(34l, new Date(), new Date(), 4.50, category, Arrays.asList(
                new ItemTranslation(null, new Date(), new Date(), "lemonade", "fresh summer drink",
                        new Language(1l, "english", "en_EN"), null),
                new ItemTranslation(null, new Date(), new Date(), "limunada", "",
                        new Language(1l, "serbian", "sr_SR"), null)
        ));
        ItemOutputDto itemDto = new ItemOutputDto(34l, 4.50, Arrays.asList(
                new ItemTranslationOutputDto("lemonade", "fresh summer drink", "english", "en_EN"),
                new ItemTranslationOutputDto("limunada", "", "serbian", "sr_SR")
        ));

        Mockito.when(itemConverter.toDto(item)).thenReturn(itemDto);
        Mockito.when(itemRepository.findById(34l)).thenReturn(Optional.of(item));

        ItemOutputDto result = itemService.getById(34l);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(itemDto, result);
    }

    @Test
    public void getItemFailureTest() {
        Mockito.when(itemRepository.findById(34l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            itemService.getById(34l);
        });
        Assertions.assertEquals("Item doesn't exist!", ex.getMessage());
    }

    @Test
    public void deleteItemByIdSuccessfullyTest() throws Exception {
        Category category = new Category(2l, "soft drinks", "0% alcohol", new Date(), new Date(),
                null, null, null, null);
        Item item = new Item(34l, new Date(), new Date(), 4.50, category, Arrays.asList(
                new ItemTranslation(null, new Date(), new Date(), "lemonade", "fresh summer drink",
                        new Language(1l, "english", "en_EN"), null),
                new ItemTranslation(null, new Date(), new Date(), "limunada", "",
                        new Language(1l, "serbian", "sr_SR"), null)
        ));

        Mockito.doNothing().when(itemRepository).delete(item);
        Mockito.when(itemRepository.findById(34l)).thenReturn(Optional.of(item));

        itemService.delete(34l);

        Mockito.verify(itemRepository, Mockito.times(1))
                .delete(ArgumentMatchers.any(Item.class));
    }

    @Test
    public void deleteItemFailureTest() {
        Mockito.when(itemRepository.findById(34l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            itemService.delete(34l);
        });
        Assertions.assertEquals("Item doesn't exist!", ex.getMessage());
    }
}