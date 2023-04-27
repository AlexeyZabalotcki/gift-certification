package ru.clevertec.zabalotcki.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.zabalotcki.builder.TagBuilder;
import ru.clevertec.zabalotcki.builder.TagDtoBuilder;
import ru.clevertec.zabalotcki.builder.TestBuilder;
import ru.clevertec.zabalotcki.dao.TagRepositoryImpl;
import ru.clevertec.zabalotcki.dto.TagDto;
import ru.clevertec.zabalotcki.mapper.TagMapper;
import ru.clevertec.zabalotcki.model.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl service;

    @Mock
    private TagRepositoryImpl repository;
    @Mock
    private TagMapper mapper;

    private static Tag tag;
    private static TagDto tagDto;

    @BeforeEach
    void setUp() {
        TestBuilder<Tag> testBuilder1 = new TagBuilder();
        TestBuilder<TagDto> testBuilder2 = new TagDtoBuilder();
        tag = testBuilder1.build();
        tagDto = testBuilder2.build();

    }

    @Test
    void checkFindAllShouldReturnListCertificates() {
        List<Tag> expectedList = new ArrayList<>(Collections.singletonList(tag));
        List<TagDto> expectedListDto = new ArrayList<>(Collections.singletonList(tagDto));

        when(repository.findAll()).thenReturn(expectedList);
        when(mapper.toDtoList(expectedList)).thenReturn(expectedListDto);

        List<TagDto> actualList = service.findAll();

        assertNotNull(actualList);

        verify(repository, times(1)).findAll();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L})
    void checkFindByIdIdShouldReturnExpectedCertificate(Long id) {
        when(repository.findById(id)).thenReturn(tag);
        when(mapper.toDto(tag)).thenReturn(tagDto);

        TagDto actual = service.findById(id);

        assertNotNull(actual);

        verify(repository, times(1)).findById(id);
    }

    @Test
    void checkSaveShouldSaveCertificate() {
        when(mapper.toEntity(tagDto)).thenReturn(tag);
        when(repository.save(tag)).thenReturn(tag);
        when(mapper.toDto(tag)).thenReturn(tagDto);

        TagDto actual = service.save(tagDto);

        assertNotNull(actual);
        verify(repository, times(1)).save(tag);
    }

    @Test
    void update() {
        when(mapper.toEntity(tagDto)).thenReturn(tag);
        when(repository.update(tag)).thenReturn(tag);
        when(mapper.toDto(tag)).thenReturn(tagDto);
        TagDto actual = service.update(tagDto);

        assertNotNull(actual);

        verify(repository, times(1)).update(tag);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void deleteById(Long id) {
        doNothing().when(repository).deleteById(id);

        service.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }
}
