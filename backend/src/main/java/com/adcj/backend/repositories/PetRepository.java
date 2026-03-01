package com.adcj.backend.repositories;

import com.adcj.backend.models.Pet;
import com.adcj.backend.models.enums.AdoptionStatus;
import com.adcj.backend.models.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
    Optional<Pet> findByImageId(String imageId);

    List<Pet> findByStatus(AdoptionStatus status);

    List<Pet> findByGender(Gender gender);

    List<Pet> findByBreedContainingIgnoreCase(String breed);
}
