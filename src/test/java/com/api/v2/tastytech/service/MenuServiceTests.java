package com.api.v2.tastytech.service;

import com.api.v2.tastytech.converter.impl.MenuConverter;
import com.api.v2.tastytech.domain.*;
import com.api.v2.tastytech.dto.*;
import com.api.v2.tastytech.repository.BrandRepository;
import com.api.v2.tastytech.repository.MenuRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

@SpringBootTest
public class MenuServiceTests {

    @Autowired
    private MenuService menuService;
    @MockBean
    private MenuConverter menuConverter;
    @MockBean
    private MenuRepository menuRepository;
    @MockBean
    private BrandRepository brandRepository;

    @Test
    public void saveMenuSuccessfullyTest() throws Exception {
        MenuInputDto menuInput = new MenuInputDto("menu-1", 3l);
        Brand brand = new Brand(3l, "my brand", new Date(), new Date());
        Menu menu = new Menu(null, new Date(), new Date(), "menu-1",
                new Brand(3l, null, null, null), null);
        Menu savedMenu = new Menu(45l, new Date(), new Date(), "menu-1", brand, null);
        MenuOutputDto menuOutput = new MenuOutputDto(45l, "menu-1", new BrandOutputDto(3l, "my brand"), null);

        Mockito.when(menuConverter.toDto(savedMenu)).thenReturn(menuOutput);
        Mockito.when(menuRepository.save(menu)).thenReturn(savedMenu);
        Mockito.when(menuConverter.toEntity(menuInput)).thenReturn(menu);
        Mockito.when(brandRepository.findById(menuInput.getBrandId())).thenReturn(Optional.of(brand));

        MenuOutputDto result = menuService.save(menuInput);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(menuOutput, result);
    }

    @Test
    public void saveMenuCantFindBrandTest() {
        MenuInputDto menuInput = new MenuInputDto("menu-1", 3l);

        Mockito.when(brandRepository.findById(menuInput.getBrandId())).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            menuService.save(menuInput);
        });
        Assertions.assertEquals("Selected brand doesn't exist!", ex.getMessage());
    }

    @Test
    public void getAllMenusSuccessfully() throws Exception {
        Brand brand = new Brand(2l, "My Brand", new Date(), new Date());
        BrandOutputDto brandDto = new BrandOutputDto(2l, "My Brand");
        List<Menu> menus = Arrays.asList(
                new Menu(8l, new Date(), new Date(), "menu", brand, null),
                new Menu(9l, new Date(), new Date(), "kids menu", brand, null)
        );
        List<MenuOutputDto> menusDto = Arrays.asList(
                new MenuOutputDto(8l, "menu", brandDto, null),
                new MenuOutputDto(9l, "kids menu", brandDto, null)
        );

        Mockito.when(menuConverter.toDto(ArgumentMatchers.any(Menu.class))).thenAnswer(invocationOnMock -> {
            Menu entity = invocationOnMock.getArgument(0);
            return new MenuOutputDto(entity.getId(), entity.getName(), brandDto, null);
        });
        Mockito.when(menuRepository.findAllByBrand(brand)).thenReturn(menus);
        Mockito.when(brandRepository.findById(2l)).thenReturn(Optional.of(brand));

        List<MenuOutputDto> result = menuService.getAll(2l);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(menusDto, result);
    }

    @Test
    public void getAllMenusCantFindBrandTest() {
        Mockito.when(brandRepository.findById(2l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            menuService.getAll(2l);
        });
        Assertions.assertEquals("Brand doesn't exist!", ex.getMessage());
    }

    @Test
    public void getAllMenusEmptyListReturnedTest() throws Exception {
        Brand brand = new Brand(2l, "My Brand", new Date(), new Date());

        Mockito.when(menuRepository.findAllByBrand(brand)).thenReturn(new ArrayList<>());
        Mockito.when(brandRepository.findById(2l)).thenReturn(Optional.of(brand));

        List<MenuOutputDto> result = menuService.getAll(2l);

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(new MenuOutputDto[0], result.toArray());
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void getMenuByIdSuccessfullyTest() throws Exception {
        Brand brand = new Brand(3l, "my brand", new Date(), new Date());
        Menu menu = new Menu(45l, new Date(), new Date(), "menu-1", brand, null);
        MenuOutputDto menuDto = new MenuOutputDto(45l, "menu-1", new BrandOutputDto(3l, "my brand"), null);

        Mockito.when(menuConverter.toDto(menu)).thenReturn(menuDto);
        Mockito.when(menuRepository.findById(45l)).thenReturn(Optional.of(menu));

        MenuOutputDto result = menuService.getById(45l);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(menuDto, result);
    }

    @Test
    public void getMenuByIdWithCategoriesTest() throws Exception {
        Brand brand = new Brand(3l, "my brand", new Date(), new Date());
        Language english = new Language(1l, "english", "en_EN");
        Language serbian = new Language(2l, "serbian", "sr_SR");
        Menu menu = new Menu(45l, new Date(), new Date(), "menu-1", brand, Arrays.asList(
                new Category(5l, "drinks", "", new Date(), new Date(), null, null, Arrays.asList(
                        new CategoryTranslation(56l, new Date(), new Date(), "drinks", "", english, null),
                        new CategoryTranslation(57l, new Date(), new Date(), "pice", "", serbian, null)
                ), Arrays.asList(
                        new Item(34l, new Date(), new Date(), 4.50, null, Arrays.asList(
                                new ItemTranslation(55l, new Date(), new Date(), "lemonade", "", english, null),
                                new ItemTranslation(56l, new Date(), new Date(), "limunada", "", serbian, null)
                        )),
                        new Item(35l, new Date(), new Date(), 8.90, null, Arrays.asList(
                                new ItemTranslation(57l, new Date(), new Date(), "orange juice", "", english, null),
                                new ItemTranslation(58l, new Date(), new Date(), "sok od narandze", "", serbian, null)
                        ))
                )
                ), new Category(6l, "food", "", new Date(), new Date(), null, null, Arrays.asList(
                        new CategoryTranslation(77l, new Date(), new Date(), "food", "", english, null),
                        new CategoryTranslation(78l, new Date(), new Date(), "hrana", "", serbian, null)
                ), null)));
        MenuOutputDto menuDto = new MenuOutputDto(45l, "menu-1", new BrandOutputDto(3l, "my brand"), Arrays.asList(
                new CategoryOutputDto(5l, "drinks", "", null, Arrays.asList(
                        new CategoryTranslationOutputDto("drinks", "", "english", "en_EN"),
                        new CategoryTranslationOutputDto("pice", "", "serbian", "sr_SR")
                ), Arrays.asList(
                        new ItemOutputDto(34l, 4.50, Arrays.asList(
                                new ItemTranslationOutputDto("lemonade", "", "english", "en_EN"),
                                new ItemTranslationOutputDto("limunada", "", "serbian", "sr_SR")
                        )),
                        new ItemOutputDto(35l, 8.90, Arrays.asList(
                                new ItemTranslationOutputDto("orange juice", "", "english", "en_EN"),
                                new ItemTranslationOutputDto("sok od narandze", "", "serbian", "sr_SR")
                        ))
                )),
                new CategoryOutputDto(6l, "food", "", null, Arrays.asList(
                        new CategoryTranslationOutputDto("food", "", "english", "en_EN"),
                        new CategoryTranslationOutputDto("hrana", "", "serbian", "sr_SR")
                ), null)
        ));

        Mockito.when(menuConverter.toDto(menu)).thenReturn(menuDto);
        Mockito.when(menuRepository.findById(45l)).thenReturn(Optional.of(menu));

        MenuOutputDto result = menuService.getById(45l);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(menuDto, result);
    }

    @Test
    public void getMenuFailureTest() {
        Mockito.when(menuRepository.findById(45l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            menuService.getById(45l);
        });
        Assertions.assertEquals("Menu doesn't exist!", ex.getMessage());
    }

    @Test
    public void updateMenuSuccessfullyTest() throws Exception {
        MenuInputDto menuInput = new MenuInputDto("zMenu", 3l);
        Brand brand = new Brand(3l, "my brand", new Date(), new Date());
        Menu dbMenu = new Menu(45l, new Date(), new Date(), "menu-1", brand, null);
        Menu convertedMenu = new Menu(null, new Date(), new Date(), "zMenu",
                new Brand(3l, null, null, null), null);
        Menu updatedMenu = new Menu(45l, new Date(), new Date(), "zMenu", brand, null);
        MenuOutputDto menuOutput = new MenuOutputDto(45l, "zMenu", new BrandOutputDto(3l, "my brand"), null);

        Mockito.when(menuConverter.toDto(updatedMenu)).thenReturn(menuOutput);
        Mockito.when(menuRepository.save(convertedMenu)).thenReturn(updatedMenu);
        Mockito.when(menuConverter.toEntity(menuInput)).thenReturn(convertedMenu);
        Mockito.when(menuRepository.findById(45l)).thenReturn(Optional.of(dbMenu));

        MenuOutputDto result = menuService.update(45l, menuInput);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(menuOutput, result);
    }

    @Test
    public void updateMenuCantFindMenuTest() {
        MenuInputDto menuInput = new MenuInputDto("menu-1", 3l);

        Mockito.when(menuRepository.findById(45l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            menuService.update(45l, menuInput);
        });
        Assertions.assertEquals("Menu doesn't exist!", ex.getMessage());
    }

    @Test
    public void updateMenuInputDtoIsNullTest() {
        MenuInputDto menuInput = null;
        Brand brand = new Brand(3l, "my brand", new Date(), new Date());
        Menu dbMenu = new Menu(45l, new Date(), new Date(), "menu-1", brand, null);

        Mockito.when(menuRepository.findById(45l)).thenReturn(Optional.of(dbMenu));
        Exception ex = Assertions.assertThrows(NullPointerException.class, () -> {
            menuService.update(45l, menuInput);
        });
        Assertions.assertInstanceOf(NullPointerException.class, ex);
    }

    @Test
    public void deleteMenuSuccessfullyTest() throws Exception {
        Brand brand = new Brand(3l, "my brand", new Date(), new Date());
        Menu dbMenu = new Menu(45l, new Date(), new Date(), "menu-1", brand, null);

        Mockito.doNothing().when(menuRepository).delete(dbMenu);
        Mockito.when(menuRepository.findById(45l)).thenReturn(Optional.of(dbMenu));

        menuService.delete(45l);

        Mockito.verify(menuRepository, Mockito.times(1))
                .delete(ArgumentMatchers.any(Menu.class));
    }

    @Test
    public void deleteMenuFailureTest()  {

        Mockito.when(menuRepository.findById(7l)).thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            menuService.delete(7l);
        });
        Assertions.assertEquals("Menu doesn't exist!", ex.getMessage());
    }
}
