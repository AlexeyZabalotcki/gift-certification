package ru.clevertec.zabalotcki.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.zabalotcki.builder.GiftCertificateBuilder;
import ru.clevertec.zabalotcki.builder.GiftCertificateDtoBuilder;
import ru.clevertec.zabalotcki.builder.TestBuilder;
import ru.clevertec.zabalotcki.dao.GiftCertificateRepositoryImpl;
import ru.clevertec.zabalotcki.dto.GiftCertificateDto;
import ru.clevertec.zabalotcki.mapper.GiftCertificateMapper;
import ru.clevertec.zabalotcki.model.GiftCertificate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @InjectMocks
    private GiftCertificateServiceImpl service;

    @Mock
    private GiftCertificateRepositoryImpl repository;
    @Mock
    private GiftCertificateMapper mapper;

    private static GiftCertificate certificate;
    private static GiftCertificateDto certificateDto;

    @BeforeEach
    void setUp() {
        TestBuilder<GiftCertificate> testBuilder1 = new GiftCertificateBuilder();
        TestBuilder<GiftCertificateDto> testBuilder2 = new GiftCertificateDtoBuilder();
        certificate = testBuilder1.build();
        certificateDto = testBuilder2.build();

    }

    @Test
    void checkFindAllShouldReturnListCertificates() {
        List<GiftCertificate> expectedList = new ArrayList<>(Collections.singletonList(certificate));
        List<GiftCertificateDto> expectedListDto = new ArrayList<>(Collections.singletonList(certificateDto));

        when(repository.findAll()).thenReturn(expectedList);
        when(mapper.toDtoList(expectedList)).thenReturn(expectedListDto);

        List<GiftCertificateDto> actualList = service.findAll();

        assertNotNull(actualList);

        verify(repository, times(1)).findAll();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L})
    void checkFindByIdIdShouldReturnExpectedCertificate(Long id) {

        when(repository.findById(id)).thenReturn(certificate);
        when(mapper.toDto(certificate)).thenReturn(certificateDto);

        GiftCertificateDto actual = service.findById(id);

        assertNotNull(actual);

        verify(repository, times(1)).findById(id);
    }

    @Test
    void checkSaveShouldSaveCertificate() {
        when(mapper.toEntity(certificateDto)).thenReturn(certificate);
        when(repository.save(certificate)).thenReturn(certificate);
        when(mapper.toDto(certificate)).thenReturn(certificateDto);

        GiftCertificateDto actual = service.save(certificateDto);

        assertNotNull(actual);
        verify(repository, times(1)).save(certificate);
    }

    @Test
    void update() {
        when(mapper.toEntity(certificateDto)).thenReturn(certificate);
        when(repository.update(certificate)).thenReturn(certificate);
        when(mapper.toDto(certificate)).thenReturn(certificateDto);
        GiftCertificateDto actual = service.update(certificateDto);

        assertNotNull(actual);

        verify(repository, times(1)).update(certificate);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void deleteById(Long id) {
        doNothing().when(repository).deleteById(id);

        service.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }


    @ParameterizedTest
    @ValueSource(strings = {"certificate", "cer", "is", "Tag"})
    void searchByNameOrDescription(String values) {
        List<GiftCertificate> expectedList = new ArrayList<>(Collections.singletonList(certificate));
        List<GiftCertificateDto> expectedListDto = new ArrayList<>(Collections.singletonList(certificateDto));

        when(repository.searchByNameOrDescriptionOrTags(values)).thenReturn(expectedList);
        when(mapper.toDtoList(expectedList)).thenReturn(expectedListDto);

        List<GiftCertificateDto> actualList = service.searchByNameOrDescription(values);

        assertNotNull(actualList);

        verify(repository, times(1)).searchByNameOrDescriptionOrTags(values);
    }
}