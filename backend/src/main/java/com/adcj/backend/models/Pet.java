package com.adcj.backend.models;

import com.adcj.backend.models.enums.AdoptionStatus;
import com.adcj.backend.models.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "pets",
        uniqueConstraints = {@UniqueConstraint(name = "pets_image_id_unique", columnNames = "image_id")}
)
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_id", nullable = false)
    private String imageId;

    @Enumerated(EnumType.STRING)
    private AdoptionStatus status;
}
