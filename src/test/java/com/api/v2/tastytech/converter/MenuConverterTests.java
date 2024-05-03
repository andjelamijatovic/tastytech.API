package com.api.v2.tastytech.converter;

import com.api.v2.tastytech.converter.impl.BrandConverter;
import com.api.v2.tastytech.converter.impl.CategoryConverter;
import com.api.v2.tastytech.converter.impl.MenuConverter;
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
public class MenuConverterTests {

    @Autowired
    private MenuConverter menuConverter;
    @MockBean
    private BrandConverter brandConverter;
    @MockBean
    private CategoryConverter categoryConverter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void toDtoNoCategoriesTest() {
        Brand brand = new Brand(3l, "Caffe Caffe", new Date(), new Date());
        Menu menu = new Menu(1l, new Date(), new Date(), "My Menu", brand, null);
        BrandOutputDto brandDto = new BrandOutputDto(3l, "Caffe Caffe");
        MenuOutputDto menuDto = new MenuOutputDto(1l, "My Menu", brandDto, null);

        Mockito.when(brandConverter.toDto(brand)).thenReturn(brandDto);

        MenuOutputDto result = menuConverter.toDto(menu);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(menuDto, result);
    }

    @Test
    public void toDtoWithCategoriesTest() {
        Brand brand = new Brand(3l, "Caffe Caffe", new Date(), new Date());
        Language english = new Language(1l, "english", "en_EN");
        Language serbian = new Language(2l, "serbian", "sr_SR");
        List<Category> categories = Arrays.asList(
                new Category(7l, "soft drinks", "", new Date(), new Date(), null, null,
                        Arrays.asList(
                                new CategoryTranslation(45l, new Date(), new Date(), "soft drinks", "", english, null),
                                new CategoryTranslation(56l, new Date(), new Date(), "bezalkoholna pica", "", serbian, null)
                        ), null),
                new Category(8l, "coffee", "", new Date(), new Date(), null, null,
                        Arrays.asList(
                                new CategoryTranslation(77l, new Date(), new Date(), "coffee", "", english, null),
                                new CategoryTranslation(78l, new Date(), new Date(), "kafe", "", serbian, null)
                        ), null)
        );
        Menu menu = new Menu(1l, new Date(), new Date(), "My Menu", brand, categories);
        BrandOutputDto brandDto = new BrandOutputDto(3l, "Caffe Caffe");
        List<CategoryOutputDto> categoriesDto = Arrays.asList(
                new CategoryOutputDto(7l, "soft drinks", "", null, Arrays.asList(
                        new CategoryTranslationOutputDto("soft drinks", "", "english", "en_EN"),
                        new CategoryTranslationOutputDto("bezalkoholna pica", "", "serbian", "sr_SR")
                ), null),
                new CategoryOutputDto(8l, "coffee", "", null, Arrays.asList(
                        new CategoryTranslationOutputDto("coffee", "", "english", "en_EN"),
                        new CategoryTranslationOutputDto("kafe", "", "serbian", "sr_SR")
                ), null)
        );
        MenuOutputDto menuDto = new MenuOutputDto(1l, "My Menu", brandDto, categoriesDto);

        Mockito.when(brandConverter.toDto(brand)).thenReturn(brandDto);
        Mockito.when(categoryConverter.toDto(ArgumentMatchers.any(Category.class))).thenAnswer(invocation -> {
            Category entity = invocation.getArgument(0);
            List<CategoryTranslationOutputDto> dtoList = entity.getTranslations()
                    .stream()
                    .map(translation -> {
                        return new CategoryTranslationOutputDto(translation.getTranslation(), translation.getDescription(),
                                translation.getLanguage().getLanguage(), translation.getLanguage().getCulturalCode());
                    })
                    .toList();
            return new CategoryOutputDto(entity.getId(), entity.getName(), entity.getDescription(), null, dtoList, null);
        });

        MenuOutputDto result = menuConverter.toDto(menu);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(menuDto, result);
    }

    @Test
    public void toEntityTest() throws Exception {
        Date formatedDate = sdf.parse(sdf.format(new Date()));

        MenuInputDto menuDto = new MenuInputDto("menu-1", 3l);
        Menu menu = new Menu(null, formatedDate, formatedDate, "menu-1", new Brand(3l, null, null, null), null);

        Menu result = menuConverter.toEntity(menuDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(menu, result);
    }
}
