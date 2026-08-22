package com.utkarsh2573.backend.visit.entity;

import com.utkarsh2573.backend.common.enums.QueueStatus;
import com.utkarsh2573.backend.doctor.entity.Doctor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "visit_queue",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_doctor_queue_date_number",
                columnNames = {"doctor_id", "queue_date", "queue_number"}
        ),
        indexes = {
                @Index(name = "idx_queue_doctor_date_status", columnList = "doctor_id,queue_date,status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "visit_id", nullable = false, unique = true)
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "queue_date", nullable = false)
    private LocalDate queueDate;

    @Column(name = "queue_number", nullable = false)
    private Integer queueNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private QueueStatus status = QueueStatus.WAITING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime calledAt;
    private LocalDateTime consultationStartedAt;
    private LocalDateTime completedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
