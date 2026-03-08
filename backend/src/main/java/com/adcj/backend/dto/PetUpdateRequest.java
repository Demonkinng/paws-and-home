package com.adcj.backend.dto;

import com.adcj.backend.models.enums.Gender;

public record PetUpdateRequest(
        String name,
        String breed,
        Integer age,
        Gender gender,
        String description
) {
}
