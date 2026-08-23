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
    public static DoctorResponse from(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getDoctorNumber(),
                doctor.getFullName(),
                doctor.getQualification(),
                doctor.getSpecialization(),
                doctor.getDepartment().getId(),
                doctor.getDepartment().getName(),
                doctor.getConsultationFee()
        );
    }
}
