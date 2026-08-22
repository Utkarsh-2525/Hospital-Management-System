package com.utkarsh2573.backend.medicine.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String medicineCode;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 100)
    private String genericName;

    @Column(length = 100)
    private String dosageForm;

    @Column(length = 100)
    private String strength;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}