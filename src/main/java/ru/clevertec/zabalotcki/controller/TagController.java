package ru.clevertec.zabalotcki.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.zabalotcki.dto.TagDto;
import ru.clevertec.zabalotcki.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final MappingJackson2HttpMessageConverter messageConverter;

    @GetMapping("/")
    public ResponseEntity<List<TagDto>> getAll() {
        List<TagDto> allGiftCertificates = tagService.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            String tagJson = messageConverter.getObjectMapper().writeValueAsString(allGiftCertificates);
            return new ResponseEntity(tagJson, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity("Error processing JSON", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findById(@PathVariable("id") Long id) {
        TagDto tagDto = tagService.findById(id);

        if (tagDto == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            String tagJson = messageConverter.getObjectMapper().writeValueAsString(tagDto);
            return new ResponseEntity(tagJson, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity("Error processing JSON", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<TagDto> add(@RequestBody String giftCertificateJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            TagDto tagDto = messageConverter.getObjectMapper()
                    .readValue(giftCertificateJson, TagDto.class);
            tagService.save(tagDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity("Error processing JSON", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<TagDto> update(@RequestBody String giftCertificateJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            TagDto tagDto = messageConverter.getObjectMapper()
                    .readValue(giftCertificateJson, TagDto.class);
            tagService.update(tagDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity("Error processing JSON", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TagDto> deleteById(@PathVariable Long id) {
        try {
            tagService.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            return new ResponseEntity("That id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
