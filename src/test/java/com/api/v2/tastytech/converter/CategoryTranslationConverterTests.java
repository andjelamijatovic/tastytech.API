package com.api.v2.tastytech.converter;

import com.api.v2.tastytech.converter.impl.CategoryTranslationConverter;
import com.api.v2.tastytech.domain.CategoryTranslation;
import com.api.v2.tastytech.domain.Language;
import com.api.v2.tastytech.dto.CategoryTranslationInputDto;
import com.api.v2.tastytech.dto.CategoryTranslationOutputDto;
import com.api.v2.tastytech.dto.ItemTranslationInputDto;
import com.api.v2.tastytech.repository.LanguageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class CategoryTranslationConverterTests {

    @Autowired
    private CategoryTranslationConverter categoryTranslationConverter;
    @MockBean
    private LanguageRepository languageRepository;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void toDtoTest() {
        CategoryTranslation translation = new CategoryTranslation(1l, new Date(), new Date(), "soft drinks",
                "zero alcohol", new Language(1l, "english", "en_US"), null);
        CategoryTranslationOutputDto translationDto = new CategoryTranslationOutputDto("soft drinks",
                "zero alcohol", "english", "en_US");

        CategoryTranslationOutputDto result = categoryTranslationConverter.toDto(translation);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(translationDto, result);
    }

    @Test
    public void toDtoEntityItemTranslationIsNullTest() {
        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            categoryTranslationConverter.toDto(null);
        });

        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }

    @Test
    public void toEntityTest() throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));

        CategoryTranslationInputDto translationDto = new CategoryTranslationInputDto("soft drinks",
                "zero alcohol", "en_US");
        Language language = new Language(1l, "english", "en_US");
        CategoryTranslation translation = new CategoryTranslation(null, formatedDate, formatedDate,
                "soft drinks", "zero alcohol", language, null);

        Mockito.when(languageRepository.findLanguageByCulturalCode(translationDto.getCulturalCode())).thenReturn(Optional.of(language));
        CategoryTranslation result = categoryTranslationConverter.toEntity(translationDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(translation, result);
    }

    @Test
    public void toEntityUnknownLanguageTest() throws Exception {

        CategoryTranslationInputDto translationDto = new CategoryTranslationInputDto("soft drinks",
                "zero alcohol", "en_US");

        Mockito.when(languageRepository.findLanguageByCulturalCode(translationDto.getCulturalCode())).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            categoryTranslationConverter.toEntity(translationDto);
        });

        Assertions.assertEquals("Unknown language!", ex.getMessage());
    }

    @Test
    public void toEntityBrandDtoIsNullTest() {
        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            categoryTranslationConverter.toEntity(null);
        });

        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }
}
