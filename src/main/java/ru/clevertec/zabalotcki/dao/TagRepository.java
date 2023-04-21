package ru.clevertec.zabalotcki.dao;

import ru.clevertec.zabalotcki.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag save(Tag tag);

    Tag update(Tag tag);

    void deleteById(Long id);

    Optional<Tag> findById(Long id);

    List<Tag> findAll();
}
