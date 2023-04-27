package ru.clevertec.zabalotcki.builder;

import ru.clevertec.zabalotcki.model.Tag;

public class TagBuilder implements TestBuilder<Tag> {

    private Long id = 1L;
    private String name = "Tag";

    @Override
    public Tag build() {
        return Tag.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }

}
