package com.adcj.backend.dto;

import com.adcj.backend.models.enums.AdoptionStatus;
import com.adcj.backend.models.enums.Gender;

public record PetDTO(
        Integer id,
        String name,
        String breed,
        Integer age,
        Gender gender,
        String description,
        String imageId,
        AdoptionStatus status
) {
}
