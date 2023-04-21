package ru.clevertec.zabalotcki.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.zabalotcki.dao.TagRepositoryImpl;
import ru.clevertec.zabalotcki.dto.TagDto;
import ru.clevertec.zabalotcki.excepton.EntityNotFoundException;
import ru.clevertec.zabalotcki.model.Tag;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepositoryImpl tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagDto save(TagDto tagDto) {
        Tag tag = tagMapper.toEntity(tagDto);
        tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    public TagDto update(TagDto tagDto) {
        Tag tag = tagMapper.toEntity(tagDto);
        tagRepository.update(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    public void deleteById(long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public TagDto findById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Tag not found with id" + id));
        return tagMapper.toDto(tag);
    }

    @Override
    public List<TagDto> findAll() {
        return tagMapper.toDtoList(tagRepository.findAll());
    }
}
