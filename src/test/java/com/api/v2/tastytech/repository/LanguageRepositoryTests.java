package com.api.v2.tastytech.repository;

import com.api.v2.tastytech.domain.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class LanguageRepositoryTests {

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    public void findLanguageByCulturalCodeTest() {

        Language language = languageRepository.save(new Language(1l, "english", "en_US"));
        Assertions.assertNotNull(language);

        Optional<Language> dbLanguage = languageRepository.findLanguageByCulturalCode("en_US");

        Assertions.assertTrue(dbLanguage.isPresent());
        Assertions.assertEquals(language, dbLanguage.get());

    }

    @Test
    public void cantFindLanguageByCulturalCodeTest() {

        Language language = languageRepository.save(new Language(1l, "english", "en_US"));
        Assertions.assertNotNull(language);

        Optional<Language> dbLanguage = languageRepository.findLanguageByCulturalCode("sr_SR");

        Assertions.assertTrue(dbLanguage.isEmpty());
    }
}
