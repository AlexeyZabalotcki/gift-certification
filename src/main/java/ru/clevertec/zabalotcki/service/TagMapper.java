package ru.clevertec.zabalotcki.service;

import org.springframework.stereotype.Component;
import ru.clevertec.zabalotcki.dto.TagDto;
import ru.clevertec.zabalotcki.model.Tag;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagMapper {

    public Tag toEntity(TagDto dto) {
        return Tag.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    public TagDto toDto(Tag entity) {
        return TagDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public List<TagDto> toDtoList(List<Tag> tags) {
        if (tags == null) {
            return null;
        }
        List<TagDto> dtos = new ArrayList<>();
        for (Tag tag : tags) {
            dtos.add(toDto(tag));
        }
        return dtos;
    }
}
