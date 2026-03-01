package com.adcj.backend.services;

import com.adcj.backend.models.Pet;
import com.adcj.backend.models.enums.AdoptionStatus;
import com.adcj.backend.repositories.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final StorageService storageService;

    public PetService(PetRepository petRepository, StorageService storageService) {
        this.petRepository = petRepository;
        this.storageService = storageService;
    }

    /**
     * Get all pets with status AVAILABLE
     *
     * @return List of available pets
     */
    public List<Pet> getAllAvailabePets() {
        return petRepository.findByStatus(AdoptionStatus.AVAILABLE);
    }

    /**
     * Get pet by id
     *
     * @param petId the id of the pet
     * @return the pet with the given id
     */
    public Pet getPetById(Integer petId) {
        return petRepository.findById(petId).orElseThrow(() -> new RuntimeException("No found pet with id: " + petId));
    }

    public void addPet(Pet pet, MultipartFile image) {
        String imageId = storageService.store(image);
        pet.setImageId(imageId);
        pet.setStatus(AdoptionStatus.AVAILABLE);
        petRepository.save(pet);
    }
}
