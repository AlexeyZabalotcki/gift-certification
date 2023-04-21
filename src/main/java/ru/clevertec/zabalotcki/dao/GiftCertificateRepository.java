package ru.clevertec.zabalotcki.dao;

import ru.clevertec.zabalotcki.model.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {
    void save(GiftCertificate certificate);

    void update(GiftCertificate certificate);

    void deleteById(Long id);

    Optional<GiftCertificate> findById(Long id);

    List<GiftCertificate> findAll();

    List<GiftCertificate> searchByNameOrDescription(String query);

    List<GiftCertificate> getAllSorted(String sortBy);
}
