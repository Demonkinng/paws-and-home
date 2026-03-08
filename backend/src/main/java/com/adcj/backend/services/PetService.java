package com.adcj.backend.services;

import com.adcj.backend.exceptions.RequestValidationException;
import com.adcj.backend.exceptions.ResourceNotFoundException;
import com.adcj.backend.models.Pet;
import com.adcj.backend.dto.PetDTO;
import com.adcj.backend.mappers.PetDTOMapper;
import com.adcj.backend.dto.PetCreateRequest;
import com.adcj.backend.dto.PetUpdateRequest;
import com.adcj.backend.models.enums.AdoptionStatus;
import com.adcj.backend.models.enums.Gender;
import com.adcj.backend.repositories.PetRepository;
import com.adcj.backend.specifications.PetSpecification;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final PetDTOMapper petDTOMapper;
    private final StorageService storageService;

    public PetService(PetRepository petRepository, StorageService storageService, PetDTOMapper petDTOMapper) {
        this.petRepository = petRepository;
        this.petDTOMapper = petDTOMapper;
        this.storageService = storageService;
    }

    private Pet getPet(Integer petId) {
        return petRepository
                .findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("No found pet with id: " + petId));
    }

    /**
     * Get pets with optional filters.
     *
     * @param status   adoption status filter.
     * @param gender   gender filter.
     * @param breed    breed filter (partial match).
     * @param search   search term to match against name, breed, or description.
     * @param pageable pagination information.
     * @return a page of PetDTOs matching the filters.
     */
    public Page<PetDTO> getPets(AdoptionStatus status, Gender gender, String breed, String search, Pageable pageable) {
        Specification<Pet> specification = Specification
                .where(PetSpecification.hasStatus(status))
                .and(PetSpecification.hasGender(gender))
                .and(PetSpecification.breedContains(breed))
                .and(PetSpecification.searchByText(search));

        return petRepository.findAll(specification, pageable).map(petDTOMapper);
    }

    public PetDTO getPetById(Integer petId) {
        Pet pet = getPet(petId);
        return petDTOMapper.apply(pet);
    }

    @Transactional
    public PetDTO addPet(PetCreateRequest request, MultipartFile image) {
        String imageId = storageService.store(image);

        Pet pet = new Pet();
        pet.setName(request.name());
        pet.setBreed(request.breed());
        pet.setAge(request.age());
        pet.setGender(request.gender());
        pet.setDescription(request.description());
        pet.setImageId(imageId);
        pet.setStatus(AdoptionStatus.AVAILABLE);

        return petDTOMapper.apply(petRepository.save(pet));
    }

    @Transactional
    public PetDTO updatePet(Integer petId, PetUpdateRequest request) {
        Pet pet = getPet(petId);

        boolean changes = false;

        if (request.name() != null && !request.name().equals(pet.getName())) {
            pet.setName(request.name());
            changes = true;
        }

        if (request.breed() != null && !request.breed().equals(pet.getBreed())) {
            pet.setBreed(request.breed());
            changes = true;
        }

        if (request.age() != null && !request.age().equals(pet.getAge())) {
            pet.setAge(request.age());
            changes = true;
        }

        if (request.description() != null && !request.description().equals(pet.getDescription())) {
            pet.setDescription(request.description());
            changes = true;
        }

        if (request.gender() != null && !request.gender().equals(pet.getGender())) {
            pet.setGender(request.gender());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes detected");
        }

        return petDTOMapper.apply(petRepository.save(pet));
    }

    @Transactional
    public PetDTO updatePetStatus(Integer petId, AdoptionStatus status) {
        Pet pet = getPet(petId);

        if (status == null || status.equals(pet.getStatus())) {
            throw new RequestValidationException("Invalid or no status change detected");
        }

        pet.setStatus(status);
        return petDTOMapper.apply(petRepository.save(pet));
    }

    @Transactional
    public void uploadPetImage(Integer petId, MultipartFile image) {
        Pet pet = getPet(petId);
        storageService.delete(pet.getImageId()); // delete old image
        String newImageId = storageService.store(image);
        pet.setImageId(newImageId);
        petRepository.save(pet);
    }

    public Resource getPetImage(Integer petId) {
        Pet pet = getPet(petId);
        return storageService.loadAsResource(pet.getImageId());
    }

    @Transactional
    public void deletePet(Integer petId) {
        Pet pet = getPet(petId);
        // delete image before deleting pet
        storageService.delete(pet.getImageId());
        petRepository.delete(pet);
    }
}
