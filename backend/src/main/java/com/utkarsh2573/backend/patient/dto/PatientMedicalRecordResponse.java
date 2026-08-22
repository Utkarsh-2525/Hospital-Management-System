package com.utkarsh2573.backend.patient.dto;

import com.utkarsh2573.backend.laboratory.dto.LabOrderResponse;
import com.utkarsh2573.backend.pharmacy.dto.PharmacyDispenseResponse;

import java.util.List;

public record PatientMedicalRecordResponse(

        PatientSummary patient,

        List<PatientVisitSummary> visits,

        List<PatientPrescriptionResponse> prescriptions,

        List<PharmacyDispenseResponse> pharmacy,

        List<LabOrderResponse> labOrders
) {

    public record PatientSummary(
            String patientNumber,
            String fullName,
            String bloodGroup
    ) {
    }
}