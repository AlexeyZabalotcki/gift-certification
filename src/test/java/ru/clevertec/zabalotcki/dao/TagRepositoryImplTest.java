package ru.clevertec.zabalotcki.dao;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.zabalotcki.builder.TagBuilder;
import ru.clevertec.zabalotcki.builder.TestBuilder;
import ru.clevertec.zabalotcki.config.AppConfig;
import ru.clevertec.zabalotcki.model.Tag;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
class TagRepositoryImplTest {

    @Autowired
    private TagRepositoryImpl tagRepository;

    private static Tag tag;

    @BeforeEach
    void setUp() {
        TestBuilder<Tag> tagBuilder = new TagBuilder();
        tag = tagBuilder.build();
        tagRepository.deleteAll();
        tagRepository.save(tag);
    }

    @AfterEach
    void tearDown() {
        tagRepository.deleteAll();
    }

    @Test
    void checkFindAllShouldReturnExpectedList() {
        List<Tag> certificates = tagRepository.findAll();
        assertEquals(1, certificates.size());
    }

    @Test
    void checkFindByIdShouldReturnExpectedItem() {
        Tag byId = tagRepository.findById(tag.getId());
        assertEquals(tag.getId(), byId.getId());
    }

    @Test
    void checkSaveShouldAddNewItemToDb() {
        List<Tag> beforeSave = tagRepository.findAll();
        int sizeBeforeSave = beforeSave.size();
        tagRepository.save(tag);
        List<Tag> afterSave = tagRepository.findAll();
        int sizeAfterSave = afterSave.size();
        int expectedSize = sizeBeforeSave + 1;
        assertEquals(expectedSize, sizeAfterSave);
    }

    @Test
    void checkUpdateShouldUpdateItemFromDb() {
        Tag byId = tagRepository.findById(tag.getId());
        byId.setName("Name 1");
        tagRepository.update(byId);
        Tag expected = tagRepository.findById(byId.getId());
        assertNotEquals(expected, tag);
    }

    @Test
    void checkDeleteByIdShouldDeleteItemFromDb() {
        tagRepository.deleteById(tag.getId());
        assertThrows(NoResultException.class, () -> tagRepository.findById(tag.getId()));
    }

    @Test
    void checkDeleteAllShouldDeleteAllItemsFromDb() {
        tagRepository.deleteAll();
        List<Tag> all = tagRepository.findAll();
        int expected = 0;
        int actual = all.size();
        assertEquals(expected, actual);
    }
}
