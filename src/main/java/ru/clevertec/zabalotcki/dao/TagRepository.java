package ru.clevertec.zabalotcki.dao;

import ru.clevertec.zabalotcki.model.Tag;

import java.util.List;

public interface TagRepository {

    Tag save(Tag tag);

    Tag update(Tag tag);

    void deleteById(Long id);

    Tag findById(Long id);

    List<Tag> findAll();
}
