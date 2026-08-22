package com.utkarsh2573.backend.pharmacy.entity;

import com.utkarsh2573.backend.medicine.entity.Medicine;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pharmacy_dispense_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyDispenseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dispense_id", nullable = false)
    private PharmacyDispense dispense;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(nullable = false)
    private Integer prescribedQuantity;

    @Column(nullable = false)
    private Integer dispensedQuantity;

    @Column(length = 500)
    private String notes;
}
