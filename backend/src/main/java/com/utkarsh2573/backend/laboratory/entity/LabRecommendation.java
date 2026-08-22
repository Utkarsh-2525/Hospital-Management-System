package com.utkarsh2573.backend.laboratory.entity;

import com.utkarsh2573.backend.consultation.entity.Consultation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lab_recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lab_test_id", nullable = false)
    private LabTest labTest;

    @Column(length = 500)
    private String instructions;
}