package com.adcj.backend.repositories;

import com.adcj.backend.models.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer>, JpaSpecificationExecutor<Pet> {
    Optional<Pet> findByImageId(String imageId);
}
