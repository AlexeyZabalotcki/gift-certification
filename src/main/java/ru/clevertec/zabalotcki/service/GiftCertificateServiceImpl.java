package ru.clevertec.zabalotcki.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.zabalotcki.dao.GiftCertificateRepositoryImpl;
import ru.clevertec.zabalotcki.dto.GiftCertificateDto;
import ru.clevertec.zabalotcki.mapper.GiftCertificateMapper;
import ru.clevertec.zabalotcki.model.GiftCertificate;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepositoryImpl giftCertificateRepository;
    private final GiftCertificateMapper giftCertificateMapper;

    @Override
    public GiftCertificateDto save(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateMapper.toEntity(giftCertificateDto);
        GiftCertificate save = giftCertificateRepository.save(giftCertificate);
        return giftCertificateMapper.toDto(save);
    }

    @Override
    public GiftCertificateDto update(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateMapper.toEntity(giftCertificateDto);
        GiftCertificate update = giftCertificateRepository.update(giftCertificate);
        return giftCertificateMapper.toDto(update);
    }

    @Override
    public void deleteById(long id) {
        giftCertificateRepository.deleteById(id);
    }

    @Override
    public GiftCertificateDto findById(Long id) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id);
        return giftCertificateMapper.toDto(giftCertificate);
    }

    @Override
    public List<GiftCertificateDto> findAll() {
        List<GiftCertificate> all = giftCertificateRepository.findAll();
        return giftCertificateMapper.toDtoList(all);
    }

    @Override
    public List<GiftCertificateDto> searchByNameOrDescription(String query) {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.searchByNameOrDescriptionOrTags(query);
        return giftCertificateMapper.toDtoList(giftCertificates);
    }

    @Override
    public List<GiftCertificateDto> getAllSorted(String sortBy) {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll()
                .stream().sorted(Comparator.comparing(giftCertificate -> {
                    Field field = null;
                    try {
                        field = GiftCertificateDto.class.getDeclaredField(sortBy);
                        field.setAccessible(true);
                        return (Comparable<? super Object>) field.get(giftCertificateMapper.toDto(giftCertificate));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    } finally {
                        Objects.requireNonNull(field).setAccessible(false);
                    }
                }))
                .collect(Collectors.toList());
        return giftCertificateMapper.toDtoList(giftCertificates);
    }
}
