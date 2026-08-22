package com.utkarsh2573.backend.doctor.dto;

import com.utkarsh2573.backend.doctor.entity.Doctor;

import java.math.BigDecimal;

public record DoctorResponse(
        Long id,
        String doctorNumber,
        String fullName,
        String qualification,
        String specialization,
        Long departmentId,
        String departmentName,
        BigDecimal consultationFee
) {
    public static DoctorResponse from(Doctor d) {
        return new DoctorResponse(
                d.getId(), d.getDoctorNumber(), d.getFullName(),
                d.getQualification(), d.getSpecialization(),
                d.getDepartment().getId(), d.getDepartment().getName(),
                d.getConsultationFee()
        );
    }
}
