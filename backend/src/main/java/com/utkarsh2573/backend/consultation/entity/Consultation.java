package com.utkarsh2573.backend.consultation.entity;

import com.utkarsh2573.backend.common.enums.ConsultationStatus;
import com.utkarsh2573.backend.doctor.entity.Doctor;
import com.utkarsh2573.backend.visit.entity.Visit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "visit_id", nullable = false, unique = true)
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private ConsultationStatus status = ConsultationStatus.DRAFT;

    @Column(length = 2000)
    private String chiefComplaint;

    @Column(length = 3000)
    private String clinicalNotes;

    @Column(length = 2000)
    private String diagnosis;

    @Column(length = 2000)
    private String advice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        startedAt = LocalDateTime.now();
        updatedAt = startedAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}