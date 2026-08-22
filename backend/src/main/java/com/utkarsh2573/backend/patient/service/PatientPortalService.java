package com.utkarsh2573.backend.patient.service;

import com.utkarsh2573.backend.exception.ResourceNotFoundException;
import com.utkarsh2573.backend.laboratory.dto.LabOrderResponse;
import com.utkarsh2573.backend.laboratory.dto.LabResultResponse;
import com.utkarsh2573.backend.laboratory.entity.LabOrder;
import com.utkarsh2573.backend.laboratory.repository.LabOrderRepository;
import com.utkarsh2573.backend.laboratory.repository.LabResultRepository;
import com.utkarsh2573.backend.patient.dto.PatientDashboardResponse;
import com.utkarsh2573.backend.patient.dto.PatientMedicalRecordResponse;
import com.utkarsh2573.backend.patient.dto.PatientPrescriptionResponse;
import com.utkarsh2573.backend.patient.dto.PatientVisitSummary;
import com.utkarsh2573.backend.patient.entity.Patient;
import com.utkarsh2573.backend.patient.repository.PatientRepository;
import com.utkarsh2573.backend.pharmacy.dto.PharmacyDispenseResponse;
import com.utkarsh2573.backend.pharmacy.repository.PharmacyDispenseRepository;
import com.utkarsh2573.backend.prescription.repository.PrescriptionRepository;
import com.utkarsh2573.backend.visit.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientPortalService {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PharmacyDispenseRepository pharmacyDispenseRepository;
    private final LabOrderRepository labOrderRepository;
    private final LabResultRepository labResultRepository;

    private Patient getLoggedInPatient(String username) {

        return patientRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient profile not found for logged-in user"
                        )
                );
    }

    // =========================================================
    // 7A - DASHBOARD
    // =========================================================

    @Transactional(readOnly = true)
    public PatientDashboardResponse getDashboard(String username) {

        Patient patient = getLoggedInPatient(username);

        List<PatientVisitSummary> visits = visitRepository
                .findByPatientIdOrderByVisitDateDescCreatedAtDesc(
                        patient.getId()
                )
                .stream()
                .map(PatientVisitSummary::from)
                .toList();

        return PatientDashboardResponse.from(
                patient,
                visits
        );
    }

    // =========================================================
    // 7A - VISITS
    // =========================================================

    @Transactional(readOnly = true)
    public List<PatientVisitSummary> getVisits(String username) {

        Patient patient = getLoggedInPatient(username);

        return visitRepository
                .findByPatientIdOrderByVisitDateDescCreatedAtDesc(
                        patient.getId()
                )
                .stream()
                .map(PatientVisitSummary::from)
                .toList();
    }

    // =========================================================
    // 7B - PRESCRIPTIONS
    // =========================================================

    @Transactional(readOnly = true)
    public List<PatientPrescriptionResponse> getPrescriptions(
            String username
    ) {

        Patient patient = getLoggedInPatient(username);

        return prescriptionRepository
                .findByPatientIdOrderByPrescribedAtDesc(
                        patient.getId()
                )
                .stream()
                .map(PatientPrescriptionResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PharmacyDispenseResponse> getPharmacyHistory(
            String username
    ) {

        Patient patient = getLoggedInPatient(username);

        return pharmacyDispenseRepository
                .findByPatientIdOrderByIdDesc(patient.getId())
                .stream()
                .map(PharmacyDispenseResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LabOrderResponse> getLabOrders(String username) {

        Patient patient = getLoggedInPatient(username);

        return labOrderRepository
                .findByPatientIdOrderByOrderedAtDesc(patient.getId())
                .stream()
                .map(LabOrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public LabResultResponse getLabResult(
            String username,
            Long orderId
    ) {

        Patient patient = getLoggedInPatient(username);

        LabOrder order = labOrderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab order not found: " + orderId
                        )
                );

        /*
         * Security check:
         * the requested lab order must belong to
         * the authenticated patient.
         */
        if (!order.getPatient().getId().equals(patient.getId())) {

            throw new ResourceNotFoundException(
                    "Lab order not found: " + orderId
            );
        }

        return labResultRepository
                .findByLabOrderId(orderId)
                .map(LabResultResponse::from)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab result not found for order: "
                                        + orderId
                        )
                );
    }

    @Transactional(readOnly = true)
    public PatientMedicalRecordResponse getMedicalRecord(
            String username
    ) {

        Patient patient = getLoggedInPatient(username);

        List<PatientVisitSummary> visits =
                visitRepository
                        .findByPatientIdOrderByVisitDateDescCreatedAtDesc(
                                patient.getId()
                        )
                        .stream()
                        .map(PatientVisitSummary::from)
                        .toList();

        List<PatientPrescriptionResponse> prescriptions =
                prescriptionRepository
                        .findByPatientIdOrderByPrescribedAtDesc(
                                patient.getId()
                        )
                        .stream()
                        .map(PatientPrescriptionResponse::from)
                        .toList();

        List<PharmacyDispenseResponse> pharmacy =
                pharmacyDispenseRepository
                        .findByPatientIdOrderByIdDesc(
                                patient.getId()
                        )
                        .stream()
                        .map(PharmacyDispenseResponse::from)
                        .toList();

        List<LabOrderResponse> labOrders =
                labOrderRepository
                        .findByPatientIdOrderByOrderedAtDesc(
                                patient.getId()
                        )
                        .stream()
                        .map(LabOrderResponse::from)
                        .toList();

        return new PatientMedicalRecordResponse(

                new PatientMedicalRecordResponse.PatientSummary(
                        patient.getPatientNumber(),
                        patient.getFullName(),
                        patient.getBloodGroup()
                ),

                visits,
                prescriptions,
                pharmacy,
                labOrders
        );
    }
}