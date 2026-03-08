package com.adcj.backend.specifications;

import com.adcj.backend.models.Pet;
import com.adcj.backend.models.enums.AdoptionStatus;
import com.adcj.backend.models.enums.Gender;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification Pattern Implementation.
 * Utility class for building dynamic JPA Specifications for the Pet entity.
 * Provides methods to create specifications based on various filtering criteria.
 */
public class PetSpecification {
    private PetSpecification() {
    }

    public static Specification<Pet> hasStatus(AdoptionStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Pet> hasGender(Gender gender) {
        return (root, query, criteriaBuilder) ->
                gender == null ? null : criteriaBuilder.equal(root.get("gender"), gender);
    }

    public static Specification<Pet> breedContains(String breed) {
        return (root, query, criteriaBuilder) ->
                (breed == null || breed.isBlank()) ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("breed")), "%" + breed.toLowerCase() + "%");
    }

    /**
     * Specification for searching pets by name, breed, or description.
     *
     * @param search The search term.
     * @return A Specification that matches pets where the name, breed, or description contains the search term.
     */
    public static Specification<Pet> searchByText(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isBlank()) return null;
            String pattern = "%" + search.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("breed")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
            );
        };
    }
}
