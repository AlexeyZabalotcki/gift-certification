package ru.clevertec.zabalotcki.service;

import ru.clevertec.zabalotcki.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto save(GiftCertificateDto giftCertificateDTO);

    GiftCertificateDto update(GiftCertificateDto giftCertificateDTO);

    void deleteById(long id);

    GiftCertificateDto findById(Long id);

    List<GiftCertificateDto> getAllSorted(String sortBy);

    List<GiftCertificateDto> findAll();

    List<GiftCertificateDto> searchByNameOrDescription(String query);
}
