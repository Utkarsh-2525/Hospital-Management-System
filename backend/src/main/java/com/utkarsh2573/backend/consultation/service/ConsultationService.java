package com.utkarsh2573.backend.consultation.service;

import com.utkarsh2573.backend.common.enums.*;
import com.utkarsh2573.backend.consultation.dto.*;
import com.utkarsh2573.backend.consultation.entity.*;
import com.utkarsh2573.backend.consultation.repository.*;
import com.utkarsh2573.backend.doctor.entity.Doctor;
import com.utkarsh2573.backend.doctor.repository.DoctorRepository;
import com.utkarsh2573.backend.exception.*;
import com.utkarsh2573.backend.visit.entity.Visit;
import com.utkarsh2573.backend.visit.entity.VisitQueue;
import com.utkarsh2573.backend.visit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final VitalRepository vitalRepository;
    private final VisitRepository visitRepository;
    private final VisitQueueRepository queueRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public ConsultationResponse start(Long visitId, String username) {
        Visit visit = getVisit(visitId);
        Doctor doctor = getDoctorForUsername(username);

        if (!visit.getDoctor().getId().equals(doctor.getId())) {
            throw new BadRequestException("This visit is not assigned to the logged-in doctor");
        }

        if (visit.getStatus() != VisitStatus.WAITING) {
            throw new BadRequestException(
                    "Only a WAITING visit can be started. Current status: " + visit.getStatus());
        }

        VisitQueue queue = queueRepository.findByVisitId(visitId)
                .orElseThrow(() -> new BadRequestException("Queue entry not found"));

        queue.setStatus(QueueStatus.IN_CONSULTATION);
        queue.setConsultationStartedAt(LocalDateTime.now());

        Consultation consultation = consultationRepository.findByVisitId(visitId)
                .orElseGet(() -> Consultation.builder()
                        .visit(visit)
                        .doctor(doctor)
                        .status(ConsultationStatus.IN_PROGRESS)
                        .build());

        consultation.setStatus(ConsultationStatus.IN_PROGRESS);
        visit.setStatus(VisitStatus.IN_CONSULTATION);

        queueRepository.save(queue);
        visitRepository.save(visit);

        return ConsultationResponse.from(consultationRepository.save(consultation));
    }

    @Transactional
    public ConsultationResponse complete(
            Long visitId,
            CompleteConsultationRequest request,
            String username
    ) {
        Visit visit = getVisit(visitId);
        Doctor doctor = getDoctorForUsername(username);

        if (!visit.getDoctor().getId().equals(doctor.getId())) {
            throw new BadRequestException("This visit is not assigned to the logged-in doctor");
        }

        Consultation consultation = consultationRepository.findByVisitId(visitId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation not found for visit: " + visitId));

        if (consultation.getStatus() != ConsultationStatus.IN_PROGRESS) {
            throw new BadRequestException("Consultation is not in progress");
        }

        consultation.setChiefComplaint(request.chiefComplaint());
        consultation.setClinicalNotes(request.clinicalNotes());
        consultation.setDiagnosis(request.diagnosis());
        consultation.setAdvice(request.advice());
        consultation.setStatus(ConsultationStatus.COMPLETED);
        consultation.setCompletedAt(LocalDateTime.now());

        VisitQueue queue = queueRepository.findByVisitId(visitId)
                .orElseThrow(() -> new BadRequestException("Queue entry not found"));

        queue.setStatus(QueueStatus.COMPLETED);
        queue.setCompletedAt(LocalDateTime.now());

        visit.setStatus(VisitStatus.CONSULTATION_COMPLETED);

        queueRepository.save(queue);
        visitRepository.save(visit);

        return ConsultationResponse.from(consultationRepository.save(consultation));
    }

    @Transactional
    public ConsultationResponse saveVitals(
            Long visitId,
            VitalRequest request,
            String username
    ) {
        Consultation consultation = getAuthorizedConsultation(visitId, username);

        Vital vital = vitalRepository.findByConsultationId(consultation.getId())
                .orElseGet(() -> Vital.builder()
                        .consultation(consultation)
                        .build());

        vital.setTemperatureC(request.temperatureC());
        vital.setSystolicBp(request.systolicBp());
        vital.setDiastolicBp(request.diastolicBp());
        vital.setPulseBpm(request.pulseBpm());
        vital.setRespiratoryRate(request.respiratoryRate());
        vital.setOxygenSaturation(request.oxygenSaturation());
        vital.setWeightKg(request.weightKg());
        vital.setHeightCm(request.heightCm());
        vital.setRecordedAt(LocalDateTime.now());

        vitalRepository.save(vital);

        return ConsultationResponse.from(consultation);
    }

    @Transactional(readOnly = true)
    public ConsultationResponse get(Long visitId, String username) {
        return ConsultationResponse.from(getAuthorizedConsultation(visitId, username));
    }

    private Consultation getAuthorizedConsultation(Long visitId, String username) {
        Consultation consultation = consultationRepository.findByVisitId(visitId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation not found for visit: " + visitId));

        Doctor doctor = getDoctorForUsername(username);

        if (!consultation.getDoctor().getId().equals(doctor.getId())) {
            throw new BadRequestException("You are not assigned to this consultation");
        }

        return consultation;
    }

    private Visit getVisit(Long visitId) {
        return visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Visit not found: " + visitId));
    }

    private Doctor getDoctorForUsername(String username) {
        return doctorRepository.findAll().stream()
                .filter(d -> d.getUser().getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor profile not found for user: " + username));
    }
}
