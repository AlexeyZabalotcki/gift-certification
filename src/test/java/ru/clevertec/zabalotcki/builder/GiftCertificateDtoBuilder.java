package ru.clevertec.zabalotcki.builder;

import ru.clevertec.zabalotcki.dto.GiftCertificateDto;
import ru.clevertec.zabalotcki.model.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GiftCertificateDtoBuilder implements TestBuilder<GiftCertificateDto> {

    private Long id = 1L;
    private String name = "certificate";
    private String description = "This is a certificate";
    private BigDecimal price = new BigDecimal(12.0);
    private int duration = 10;
    private LocalDateTime createDate = LocalDateTime.now();
    private LocalDateTime lastUpdateDate = LocalDateTime.now();
    private List<String> tags = GiftCertificateDtoBuilder.fillList();

    @Override
    public GiftCertificateDto build() {
        return GiftCertificateDto.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .duration(this.duration)
                .createDate(this.createDate)
                .lastUpdateDate(this.lastUpdateDate)
                .tags(this.tags)
                .build();
    }

    private static List<String> fillList() {
        List<String> tags = new ArrayList<>();
        Tag tag = Tag.builder()
                .id(1L)
                .name("TagDto")
                .build();
        tags.add(tag.getName());
        return tags;
    }
}
