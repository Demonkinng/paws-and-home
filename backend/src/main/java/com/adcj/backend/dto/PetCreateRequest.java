package com.adcj.backend.dto;

import com.adcj.backend.models.enums.Gender;

public record PetCreateRequest(
        String name,
        String breed,
        Integer age,
        Gender gender,
        String description
) {
}
