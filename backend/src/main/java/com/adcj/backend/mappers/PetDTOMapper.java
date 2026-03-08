package com.adcj.backend.mappers;

import com.adcj.backend.models.Pet;
import com.adcj.backend.dto.PetDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PetDTOMapper implements Function<Pet, PetDTO> {
    @Override
    public PetDTO apply(Pet pet) {
        return new PetDTO(
                pet.getId(),
                pet.getName(),
                pet.getBreed(),
                pet.getAge(),
                pet.getGender(),
                pet.getDescription(),
                pet.getImageId(),
                pet.getStatus()
        );
    }
}
