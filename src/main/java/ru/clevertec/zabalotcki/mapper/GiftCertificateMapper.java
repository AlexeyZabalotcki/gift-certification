package ru.clevertec.zabalotcki.mapper;

import org.springframework.stereotype.Component;
import ru.clevertec.zabalotcki.dto.GiftCertificateDto;
import ru.clevertec.zabalotcki.model.GiftCertificate;
import ru.clevertec.zabalotcki.model.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GiftCertificateMapper {

    public GiftCertificateDto toDto(GiftCertificate entity) {
        GiftCertificateDto dto = new GiftCertificateDto();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setDuration(entity.getDuration());
        dto.setCreateDate(entity.getCreateDate());
        dto.setLastUpdateDate(entity.getLastUpdateDate());
        if (entity.getTags() != null) {
            dto.setTags(entity.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        } else {
            dto.setTags(Collections.emptyList());
        }
        return dto;
    }

    public GiftCertificate toEntity(GiftCertificateDto dto) {
        List<Tag> tags = dto.getTags() != null ? dto.getTags().stream().map(tagName -> {
            Tag tag = new Tag();
            tag.setName(tagName);
            return tag;
        }).collect(Collectors.toList()) : null;
            return GiftCertificate.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .price(dto.getPrice())
                    .duration(dto.getDuration())
                    .createDate(dto.getCreateDate())
                    .lastUpdateDate(dto.getLastUpdateDate())
                    .tags(tags)
                    .build();
        }

    public List<GiftCertificateDto> toDtoList(List<GiftCertificate> giftCertificates) {
        if (giftCertificates == null) {
            return null;
        }
        List<GiftCertificateDto> giftCertificateDtos = new ArrayList<>();
        for (GiftCertificate giftCertificate : giftCertificates) {
            giftCertificateDtos.add(toDto(giftCertificate));
        }

        return giftCertificateDtos;
    }
}
