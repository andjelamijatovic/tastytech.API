package com.api.v2.tastytech.converter;

import com.api.v2.tastytech.converter.impl.ItemTranslationConverter;
import com.api.v2.tastytech.domain.ItemTranslation;
import com.api.v2.tastytech.domain.Language;
import com.api.v2.tastytech.dto.ItemTranslationInputDto;
import com.api.v2.tastytech.dto.ItemTranslationOutputDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class ItemTranslationConverterTests {

    @Autowired
    private ItemTranslationConverter itemTranslationConverter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void toDtoTest() {
        ItemTranslation translation = new ItemTranslation(1l, new Date(), new Date(), "lemonade",
                "fresh summer drink", new Language(1l, "english", "en_US"), null);
        ItemTranslationOutputDto translationDto = new ItemTranslationOutputDto("lemonade", "fresh summer drink",
                "english", "en_US");

        ItemTranslationOutputDto result = itemTranslationConverter.toDto(translation);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(translationDto, result);
    }

    @Test
    public void toDtoEntityItemTranslationIsNullTest() {
        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            itemTranslationConverter.toDto(null);
        });

        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }

    @Test
    public void toEntityTest() throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));

        ItemTranslationInputDto translationDto = new ItemTranslationInputDto("lemonade",
                "fresh summer drink", "en_US");
        ItemTranslation translation = new ItemTranslation(null, formatedDate, formatedDate, "lemonade",
                "fresh summer drink", null, null);

        ItemTranslation result = itemTranslationConverter.toEntity(translationDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(translation, result);
    }

    @Test
    public void toEntityBrandDtoIsNullTest() {
        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            itemTranslationConverter.toEntity(null);
        });

        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }
}
