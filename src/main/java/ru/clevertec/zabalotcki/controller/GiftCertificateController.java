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
import ru.clevertec.zabalotcki.dto.GiftCertificateDto;
import ru.clevertec.zabalotcki.service.GiftCertificateService;

import java.util.List;

@RestController
@RequestMapping("/gifts")
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final MappingJackson2HttpMessageConverter messageConverter;

    @GetMapping("/")
    public ResponseEntity<List<GiftCertificateDto>> getAll() {
        List<GiftCertificateDto> allGiftCertificates = giftCertificateService.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            String certificatesJson = messageConverter.getObjectMapper().writeValueAsString(allGiftCertificates);
            return new ResponseEntity(certificatesJson, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity("Error processing JSON", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> findById(@PathVariable("id") Long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.findById(id);

        if (giftCertificateDto == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            String certificatesJson = messageConverter.getObjectMapper().writeValueAsString(giftCertificateDto);
            return new ResponseEntity(certificatesJson, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity("Error processing JSON", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<GiftCertificateDto> add(@RequestBody String giftCertificateJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            GiftCertificateDto giftCertificateDTO = messageConverter.getObjectMapper()
                    .readValue(giftCertificateJson, GiftCertificateDto.class);
            giftCertificateService.save(giftCertificateDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity("Error processing JSON", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<GiftCertificateDto> update(@RequestBody String giftCertificateJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            GiftCertificateDto giftCertificateDTO = messageConverter.getObjectMapper()
                    .readValue(giftCertificateJson, GiftCertificateDto.class);
            giftCertificateService.update(giftCertificateDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity("Error processing JSON", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GiftCertificateDto> deleteById(@PathVariable Long id) {
        try {
            giftCertificateService.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            return new ResponseEntity("That id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GiftCertificateDto>> searchGiftCertificates(@RequestParam(required = false) String q) {
        List<GiftCertificateDto> giftCertificateDtos = giftCertificateService.searchByNameOrDescription(q);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            String certificatesJson = messageConverter.getObjectMapper().writeValueAsString(giftCertificateDtos);
            return new ResponseEntity(certificatesJson, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity("Error processing JSON", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sort")
    public ResponseEntity<List<GiftCertificateDto>> getAllSorted(@RequestParam(required = false, defaultValue = "name") String sortBy) {
        List<GiftCertificateDto> allSorted = giftCertificateService.getAllSorted(sortBy);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            String certificatesJson = messageConverter.getObjectMapper().writeValueAsString(allSorted);
            return new ResponseEntity(certificatesJson, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity("Error processing JSON", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
