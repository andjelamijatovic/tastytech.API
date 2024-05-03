package com.api.v2.tastytech.converter;

import com.api.v2.tastytech.converter.impl.CategoryConverter;
import com.api.v2.tastytech.converter.impl.CategoryTranslationConverter;
import com.api.v2.tastytech.converter.impl.ItemConverter;
import com.api.v2.tastytech.converter.impl.ItemTranslationConverter;
import com.api.v2.tastytech.domain.*;
import com.api.v2.tastytech.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class CategoryConverterTests {

    @Autowired
    private CategoryConverter categoryConverter;
    @MockBean
    private CategoryTranslationConverter categoryTranslationConverter;
    @MockBean
    private ItemConverter itemConverter;
    @MockBean
    ItemTranslationConverter itemTranslationConverter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void toDtoTest() {
        Category parentCategory = new Category(1l, "drinks", "any drink you could imagine",
                new Date(), new Date(), null, null, null, null);
        List<CategoryTranslation> translations = Arrays.asList(
                new CategoryTranslation(1l, new Date(), new Date(), "soft drinks", "",
                        new Language(1l, "english", "en_US"), null),
                new CategoryTranslation(1l, new Date(), new Date(), "bezalkoholna pica", "",
                        new Language(2l, "serbian", "sr_SR"), null)
        );
        List<Item> items = Arrays.asList(
                new Item(1l, new Date(), new Date(), 4.50, null, Arrays.asList(
                        new ItemTranslation(1l, new Date(), new Date(), "lemonade", "fresh summer drink",
                                new Language(1l, "english", "en_US"), null),
                        new ItemTranslation(2l, new Date(), new Date(), "limunada", "osvezavajuci letnji napitak",
                                new Language(2l, "serbian", "sr_SR"), null)
                )),
                new Item(2l, new Date(), new Date(), 9.80, null, Arrays.asList(
                        new ItemTranslation(3l, new Date(), new Date(), "orange juice", "fresh summer drink",
                                new Language(1l, "english", "en_US"), null),
                        new ItemTranslation(4l, new Date(), new Date(), "sok od narandze", "osvezavajuci letnji napitak",
                                new Language(2l, "serbian", "sr_SR"), null)
                ))
        );
        Category category = new Category(2l, "soft drinks", "0% alcohol", new Date(), new Date(),
                parentCategory, null, translations, items);

        List<CategoryTranslationOutputDto> translationsDto = Arrays.asList(
                new CategoryTranslationOutputDto("soft drinks", "", "english", "en_US"),
                new CategoryTranslationOutputDto("bezalkoholna pica", "", "serbian", "sr_SR")
        );
        List<ItemOutputDto> itemsDto = Arrays.asList(
                new ItemOutputDto(1l, 4.50, Arrays.asList(
                        new ItemTranslationOutputDto("lemonade", "fresh summer drink", "english", "en_US"),
                        new ItemTranslationOutputDto("limunada", "osvezavajuci letnji napitak", "serbian", "sr_SR")
                )),
                new ItemOutputDto(2l, 9.80, Arrays.asList(
                        new ItemTranslationOutputDto("orange juice", "fresh summer drink", "english", "en_US"),
                        new ItemTranslationOutputDto("sok od narandze", "osvezavajuci letnji napitak", "serbian", "sr_SR")
                ))
        );
        CategoryOutputDto categoryDto = new CategoryOutputDto(2l, "soft drinks", "0% alcohol", "drinks", translationsDto, itemsDto);

        Mockito.when(itemTranslationConverter.toDto(ArgumentMatchers.any(ItemTranslation.class))).thenAnswer(invocation -> {
            ItemTranslation entity = invocation.getArgument(0);
            return new ItemTranslationOutputDto(entity.getName(), entity.getDescription(), entity.getLanguage().getLanguage(), entity.getLanguage().getCulturalCode());
        });

        Mockito.when(itemConverter.toDto(ArgumentMatchers.any(Item.class))).thenAnswer(invocation -> {
            Item entity = invocation.getArgument(0);
            List<ItemTranslationOutputDto> dtoList = entity.getTranslations()
                    .stream()
                    .map(translation -> {
                        return new ItemTranslationOutputDto(translation.getName(), translation.getDescription(),
                                translation.getLanguage().getLanguage(), translation.getLanguage().getCulturalCode());
                    })
                    .toList();
            return new ItemOutputDto(entity.getId(), entity.getPrice(), dtoList);
        });

        Mockito.when(categoryTranslationConverter.toDto(ArgumentMatchers.any(CategoryTranslation.class))).thenAnswer(invocation -> {
            CategoryTranslation entity = invocation.getArgument(0);
            return new CategoryTranslationOutputDto(entity.getTranslation(), entity.getDescription(),
                    entity.getLanguage().getLanguage(), entity.getLanguage().getCulturalCode());
        });

        CategoryOutputDto result = categoryConverter.toDto(category);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(categoryDto, result);
    }

    @Test
    public void toEntityTest() throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));
        List<CategoryTranslationInputDto> translationsDto = Arrays.asList(
                new CategoryTranslationInputDto("soft drinks", "0% alcohol", "en_US"),
                new CategoryTranslationInputDto("bezalkoholna pica", "", "sr_SR")
        );
        CategoryInputDto categoryDto = new CategoryInputDto("soft drinks", "0% alcohol",
                1l, translationsDto);

        List<CategoryTranslation> translations = Arrays.asList(
                new CategoryTranslation(null, formatedDate, formatedDate, "soft drinks",
                        "0% alcohol", null, null),
                new CategoryTranslation(null, formatedDate, formatedDate, "bezalkoholna pica",
                        "", null, null)
        );
        Category category = new Category(null, "soft drinks", "0% alcohol",
                formatedDate, formatedDate, null, null, translations, null);

        Mockito.when(categoryTranslationConverter.toEntity(ArgumentMatchers.any(CategoryTranslationInputDto.class)))
                .thenAnswer(invocationOnMock -> {
                    CategoryTranslationInputDto dto = invocationOnMock.getArgument(0);
                    return new CategoryTranslation(null, formatedDate, formatedDate, dto.getTranslation(),
                            dto.getDescription(), null, null);
                });

        Category result = categoryConverter.toEntity(categoryDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(category, result);
    }
}
