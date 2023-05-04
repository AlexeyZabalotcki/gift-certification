package ru.clevertec.zabalotcki.excepton;

import lombok.ToString;

@ToString
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String massage) {
        super(massage);
    }
}
