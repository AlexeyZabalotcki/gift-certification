package ru.clevertec.zabalotcki.service;

import ru.clevertec.zabalotcki.dto.TagDto;

import java.util.List;

public interface TagService {
    TagDto save(TagDto tagDto);

    TagDto update(TagDto tagDto);

    void deleteById(long id);

    TagDto findById(Long id);


    List<TagDto> findAll();

}
