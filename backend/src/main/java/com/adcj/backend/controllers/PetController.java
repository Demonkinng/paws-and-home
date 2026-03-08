package com.adcj.backend.controllers;

import com.adcj.backend.dto.PetCreateRequest;
import com.adcj.backend.dto.PetDTO;
import com.adcj.backend.dto.PetUpdateRequest;
import com.adcj.backend.models.enums.AdoptionStatus;
import com.adcj.backend.models.enums.Gender;
import com.adcj.backend.services.PetService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/pets")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/{petId}")
    public ResponseEntity<PetDTO> getPet(@PathVariable Integer petId) {
        return ResponseEntity.ok(petService.getPetById(petId));
    }

    @GetMapping
    public ResponseEntity<Page<PetDTO>> getPets(
            @RequestParam(required = false) AdoptionStatus status,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String search,
            @PageableDefault(page = 0, size = 9) Pageable pageable) {
        return ResponseEntity.ok(petService.getPets(status, gender, breed, search, pageable));
    }

    @GetMapping(value = "/{petId}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getPetImage(@PathVariable Integer petId) {
        Resource image = petService.getPetImage(petId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
                .body(image);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetDTO> createPet(@RequestPart("pet") PetCreateRequest request,
                                            @RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.addPet(request, image));
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetDTO> updatePet(@PathVariable Integer petId, @RequestBody PetUpdateRequest request) {
        return ResponseEntity.ok(petService.updatePet(petId, request));
    }

    @PatchMapping("/{petId}/status")
    public ResponseEntity<PetDTO> updatePetStatus(@PathVariable Integer petId, @RequestParam AdoptionStatus status) {
        return ResponseEntity.ok(petService.updatePetStatus(petId, status));
    }

    @PutMapping(value = "/{petId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadPetImage(@PathVariable Integer petId, @RequestParam("image") MultipartFile image) {
        petService.uploadPetImage(petId, image);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable Integer petId) {
        petService.deletePet(petId);
        return ResponseEntity.noContent().build();
    }
}
