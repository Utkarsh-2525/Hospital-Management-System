package com.utkarsh2573.backend.laboratory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "lab_order_item_id",
            nullable = false,
            unique = true
    )
    private LabOrderItem labOrderItem;

    @Column(nullable = false, length = 5000)
    private String result;

    @Column(length = 1000)
    private String remarks;

    @Column(length = 500)
    private String attachmentUrl;

    @Column(nullable = false)
    private LocalDateTime reportedAt;
}