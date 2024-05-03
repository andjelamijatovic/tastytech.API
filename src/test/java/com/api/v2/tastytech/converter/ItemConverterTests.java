package com.api.v2.tastytech.converter;

import com.api.v2.tastytech.converter.impl.ItemConverter;
import com.api.v2.tastytech.converter.impl.ItemTranslationConverter;
import com.api.v2.tastytech.domain.Item;
import com.api.v2.tastytech.domain.ItemTranslation;
import com.api.v2.tastytech.domain.Language;
import com.api.v2.tastytech.dto.ItemInputDto;
import com.api.v2.tastytech.dto.ItemOutputDto;
import com.api.v2.tastytech.dto.ItemTranslationInputDto;
import com.api.v2.tastytech.dto.ItemTranslationOutputDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class ItemConverterTests {

    @Autowired
    private ItemConverter itemConverter;
    @MockBean
    private ItemTranslationConverter itemTranslationConverter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void toDtoTest() {
        List<ItemTranslation> translations = Arrays.asList(
                new ItemTranslation(1l, new Date(), new Date(), "lemonade", "fresh summer drink",
                        new Language(1l, "english", "en_US"), null),
                new ItemTranslation(2l, new Date(), new Date(), "limunada", "osvezavajuci letnji napitak",
                        new Language(2l, "serbian", "sr_SR"), null)
        );
        List<ItemTranslationOutputDto> translationsDto = Arrays.asList(
                new ItemTranslationOutputDto("lemonade", "fresh summer drink", "english", "en_US"),
                new ItemTranslationOutputDto("limunada", "osvezavajuci letnji napitak", "serbian", "sr_SR")
        );
        Item item = new Item(1l, new Date(), new Date(), 15.80, null, translations);
        ItemOutputDto itemDto = new ItemOutputDto(1l, 15.80, translationsDto);

        Mockito.when(itemTranslationConverter.toDto(ArgumentMatchers.any(ItemTranslation.class))).thenAnswer(invocation -> {
            ItemTranslation entity = invocation.getArgument(0);
            return new ItemTranslationOutputDto(entity.getName(), entity.getDescription(), entity.getLanguage().getLanguage(), entity.getLanguage().getCulturalCode());
        });

        ItemOutputDto result = itemConverter.toDto(item);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(itemDto, result);
    }

    @Test
    public void toDtoItemTranslationsIsNullTest() {
        Item item = new Item(1l, new Date(), new Date(), 15.80, null, null);
        ItemOutputDto itemDto = new ItemOutputDto(1l, 15.80, new ArrayList<>());

        ItemOutputDto result = itemConverter.toDto(item);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(itemDto, result);
    }

    @Test
    public void toDtoItemTranslationsIsEmptyTest() {
        Item item = new Item(1l, new Date(), new Date(), 15.80, null, new ArrayList<>());
        ItemOutputDto itemDto = new ItemOutputDto(1l, 15.80, new ArrayList<>());

        ItemOutputDto result = itemConverter.toDto(item);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(itemDto, result);
    }

    @Test
    public void toDtoEntityItemIsNullTest() {
        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            itemConverter.toDto(null);
        });

        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }

    @Test
    public void toEntityTest() throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));

        List<ItemTranslationInputDto> translationsDto = Arrays.asList(
                new ItemTranslationInputDto("lemonade", "fresh summer drink", "en_US"),
                new ItemTranslationInputDto("limunada", "osvezavajuci letnji napitak", "sr_SR")
        );
        List<ItemTranslation> translations = Arrays.asList(
                new ItemTranslation(null, formatedDate, formatedDate, "lemonade", "fresh summer drink", null, null),
                new ItemTranslation(null, formatedDate, formatedDate, "limunada", "osvezavajuci letnji napitak", null, null)
        );
        ItemInputDto itemDto = new ItemInputDto(14.80, translationsDto);
        Item item = new Item(null, formatedDate, formatedDate, 14.80, null, translations);

        Mockito.when(itemTranslationConverter.toEntity(ArgumentMatchers.any(ItemTranslationInputDto.class))).thenAnswer(invocation -> {
            ItemTranslationInputDto dto = invocation.getArgument(0);
            return new ItemTranslation(null, formatedDate, formatedDate, dto.getItem(), dto.getDescription(), null, null);
        });

        Item result = itemConverter.toEntity(itemDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(item, result);
    }

    @Test
    public void toEntityItemTranslationDtoIsNullTest() throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));

        ItemInputDto itemDto = new ItemInputDto(14.80, null);
        Item item = new Item(null, formatedDate, formatedDate, 14.80, null, new ArrayList<>());

        Item result = itemConverter.toEntity(itemDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(item, result);
    }

    @Test
    public void toEntityItemTranslationDtoIsEmptyTest() throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));

        ItemInputDto itemDto = new ItemInputDto(14.80, new ArrayList<>());
        Item item = new Item(null, formatedDate, formatedDate, 14.80, null, new ArrayList<>());

        Item result = itemConverter.toEntity(itemDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(item, result);
    }

    @Test
    public void toEntityItemDtoIsNullTest() {
        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            itemConverter.toEntity(null);
        });

        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }
}
