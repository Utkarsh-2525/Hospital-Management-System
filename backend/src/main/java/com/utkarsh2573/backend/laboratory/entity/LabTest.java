package com.utkarsh2573.backend.laboratory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lab_tests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String testCode;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 100)
    private String sampleType;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}