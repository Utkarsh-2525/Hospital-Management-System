package com.utkarsh2573.backend.doctor.entity;

import com.utkarsh2573.backend.department.entity.Department;
import com.utkarsh2573.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String doctorNumber;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(length = 100)
    private String qualification;

    @Column(length = 120)
    private String specialization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}