package ru.clevertec.zabalotcki.dao;

import ru.clevertec.zabalotcki.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateRepository {

    GiftCertificate save(GiftCertificate certificate);

    GiftCertificate update(GiftCertificate certificate);

    void deleteById(Long id);

    GiftCertificate findById(Long id);

    List<GiftCertificate> findAll();

    List<GiftCertificate> searchByNameOrDescriptionOrTags(String query);

}
