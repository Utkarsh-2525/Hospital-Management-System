package com.utkarsh2573.backend.pharmacy.entity;

import com.utkarsh2573.backend.common.enums.DispenseStatus;
import com.utkarsh2573.backend.patient.entity.Patient;
import com.utkarsh2573.backend.prescription.entity.Prescription;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pharmacy_dispenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyDispense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String dispenseNumber;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prescription_id", nullable = false, unique = true)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private DispenseStatus status = DispenseStatus.PENDING;

    @Column(length = 1000)
    private String pharmacistNotes;

    private LocalDateTime dispensedAt;

    @OneToMany(mappedBy = "dispense", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PharmacyDispenseItem> items = new ArrayList<>();
}
