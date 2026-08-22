package com.utkarsh2573.backend.consultation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vitals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consultation_id", nullable = false, unique = true)
    private Consultation consultation;

    @Column(precision = 5, scale = 2)
    private BigDecimal temperatureC;

    @Column(precision = 6, scale = 2)
    private BigDecimal systolicBp;

    @Column(precision = 6, scale = 2)
    private BigDecimal diastolicBp;

    @Column(precision = 6, scale = 2)
    private BigDecimal pulseBpm;

    @Column(precision = 6, scale = 2)
    private BigDecimal respiratoryRate;

    @Column(precision = 6, scale = 2)
    private BigDecimal oxygenSaturation;

    @Column(precision = 6, scale = 2)
    private BigDecimal weightKg;

    @Column(precision = 6, scale = 2)
    private BigDecimal heightCm;

    private LocalDateTime recordedAt;
}