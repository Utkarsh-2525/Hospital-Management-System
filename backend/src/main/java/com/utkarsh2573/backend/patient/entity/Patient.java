package com.utkarsh2573.backend.patient.entity;

import com.utkarsh2573.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String patientNumber;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 20)
    private String gender;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @Column(length = 120, unique = true)
    private String email;

    @Column(length = 500)
    private String address;

    @Column(length = 20)
    private String bloodGroup;

    @Column(length = 500)
    private String allergies;

    @Column(length = 120)
    private String emergencyContactName;

    @Column(length = 20)
    private String emergencyContactPhone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
