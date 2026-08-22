package com.utkarsh2573.backend.patient.service;

import com.utkarsh2573.backend.exception.ResourceNotFoundException;
import com.utkarsh2573.backend.patient.dto.PatientDashboardResponse;
import com.utkarsh2573.backend.patient.dto.PatientVisitSummary;
import com.utkarsh2573.backend.patient.entity.Patient;
import com.utkarsh2573.backend.patient.repository.PatientRepository;
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

    @Transactional(readOnly = true)
    public PatientDashboardResponse getDashboard(String username) {
        Patient patient = patientRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient profile not found for logged-in user"
                        ));

        List<PatientVisitSummary> visits = visitRepository
                .findByPatientIdOrderByVisitDateDescCreatedAtDesc(patient.getId())
                .stream()
                .map(PatientVisitSummary::from)
                .toList();

        return PatientDashboardResponse.from(patient, visits);
    }

    @Transactional(readOnly = true)
    public List<PatientVisitSummary> getVisits(String username) {
        Patient patient = patientRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient profile not found for logged-in user"
                        ));

        return visitRepository
                .findByPatientIdOrderByVisitDateDescCreatedAtDesc(patient.getId())
                .stream()
                .map(PatientVisitSummary::from)
                .toList();
    }
}
