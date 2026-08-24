package com.utkarsh2573.backend.laboratory.entity;

import com.utkarsh2573.backend.common.enums.LabOrderStatus;
import com.utkarsh2573.backend.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lab_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consultation_id", nullable = false)
    private com.utkarsh2573.backend.consultation.entity.Consultation consultation;

    @OneToMany(
            mappedBy = "labOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<LabOrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private LabOrderStatus status = LabOrderStatus.ORDERED;

    @Column(length = 500)
    private String instructions;

    private LocalDateTime orderedAt;
    private LocalDateTime sampleCollectedAt;
    private LocalDateTime completedAt;

    @PrePersist
    void onCreate() {
        orderedAt = LocalDateTime.now();
    }

    public void addItem(LabOrderItem item) {
        items.add(item);
        item.setLabOrder(this);
    }
}