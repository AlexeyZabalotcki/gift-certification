package ru.clevertec.zabalotcki.dao;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.zabalotcki.builder.GiftCertificateBuilder;
import ru.clevertec.zabalotcki.builder.TestBuilder;
import ru.clevertec.zabalotcki.config.AppConfig;
import ru.clevertec.zabalotcki.config.ContainersEnvironment;
import ru.clevertec.zabalotcki.model.GiftCertificate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
class GiftCertificateRepositoryImplTest extends ContainersEnvironment {

    @Autowired
    private GiftCertificateRepositoryImpl giftCertificateRepository;

    @Autowired
    private TagRepositoryImpl tagRepository;

    private static GiftCertificate certificate;

    @BeforeEach
    void setUp() {
        TestBuilder<GiftCertificate> certificateTestBuilder = new GiftCertificateBuilder();
        certificate = certificateTestBuilder.build();
        giftCertificateRepository.deleteAll();
        tagRepository.deleteAll();
        giftCertificateRepository.save(certificate);
    }

    @AfterEach
    void tearDown() {
        giftCertificateRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Test
    void checkFindAllShouldReturnExpectedList() {
        List<GiftCertificate> certificates = giftCertificateRepository.findAll();
        assertEquals(1, certificates.size());
    }

    @Test
    void checkFindByIdShouldReturnExpectedItem() {
        GiftCertificate certificateById = giftCertificateRepository.findById(certificate.getId());
        assertEquals(certificate.getId(), certificateById.getId());
    }

    @Test
    void checkSaveShouldAddNewItemToDb() {
        List<GiftCertificate> beforeSave = giftCertificateRepository.findAll();
        int sizeBeforeSave = beforeSave.size();
        giftCertificateRepository.save(certificate);
        List<GiftCertificate> afterSave = giftCertificateRepository.findAll();
        int sizeAfterSave = afterSave.size();
        int expectedSize = sizeBeforeSave + 1;
        assertEquals(expectedSize, sizeAfterSave);
    }

    @Test
    void checkUpdateShouldUpdateItemFromDb() {
        GiftCertificate byId = giftCertificateRepository.findById(certificate.getId());
        byId.setDescription("Description");
        byId.setName("Name 1");
        byId.setLastUpdateDate(LocalDateTime.now());
        giftCertificateRepository.update(byId);
        GiftCertificate expected = giftCertificateRepository.findById(byId.getId());
        assertNotEquals(expected, certificate);
    }

    @Test
    void checkDeleteByIdShouldDeleteItemFromDb() {
        giftCertificateRepository.deleteById(certificate.getId());
        assertThrows(NoResultException.class, () -> giftCertificateRepository.findById(certificate.getId()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"certificate", "cer", "is", "Tag"})
    void checkSearchByNameOrDescriptionOrTagsShouldDeleteItemFromDb(String value) {
        addNewGiftCertificate();
        List<GiftCertificate> expectedCertificates = giftCertificateRepository.searchByNameOrDescriptionOrTags(value);
        List<GiftCertificate> allCertificates = giftCertificateRepository.findAll();
        assertNotEquals(expectedCertificates, allCertificates);
    }

    private void addNewGiftCertificate() {
        TestBuilder<GiftCertificate> testBuilder = new GiftCertificateBuilder();
        GiftCertificate giftCertificate = testBuilder.build();
        giftCertificate.setDescription("1233");
        giftCertificate.setName("Something");
        giftCertificate.getTags().get(0).setName("another");
        giftCertificateRepository.save(giftCertificate);
    }

    @Test
    void checkDeleteAllShouldDeleteAllItemsFromDb() {
        giftCertificateRepository.deleteAll();
        List<GiftCertificate> all = giftCertificateRepository.findAll();
        int expected = 0;
        int actual = all.size();
        assertEquals(expected, actual);
    }
}