package com.utkarsh2573.backend.visit.service;

import com.utkarsh2573.backend.billing.entity.Invoice;
import com.utkarsh2573.backend.billing.repository.InvoiceRepository;
import com.utkarsh2573.backend.common.enums.*;
import com.utkarsh2573.backend.department.entity.Department;
import com.utkarsh2573.backend.doctor.entity.Doctor;
import com.utkarsh2573.backend.doctor.repository.DoctorRepository;
import com.utkarsh2573.backend.exception.*;
import com.utkarsh2573.backend.patient.entity.Patient;
import com.utkarsh2573.backend.patient.repository.PatientRepository;
import com.utkarsh2573.backend.visit.dto.*;
import com.utkarsh2573.backend.visit.entity.*;
import com.utkarsh2573.backend.visit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final VisitRepository visitRepository;
    private final VisitQueueRepository queueRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public VisitResponse createVisit(CreateVisitRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found: " + request.patientId()));

        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found: " + request.doctorId()));

        if (!doctor.isActive()) {
            throw new BadRequestException("Selected doctor is inactive");
        }

        Department department = doctor.getDepartment();

        Visit visit = Visit.builder()
                .visitNumber(generateVisitNumber())
                .patient(patient)
                .doctor(doctor)
                .department(department)
                .visitDate(LocalDate.now())
                .status(VisitStatus.PAYMENT_PENDING)
                .consultationFee(doctor.getConsultationFee())
                .reason(request.reason())
                .build();

        return VisitResponse.from(visitRepository.save(visit));
    }

    @Transactional
    public QueueResponse generateQueue(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Visit not found: " + visitId));

        if (visit.getStatus() != VisitStatus.PAID) {
            throw new BadRequestException(
                    "Payment must be completed before generating a doctor queue entry");
        }

        if (queueRepository.findByVisitId(visitId).isPresent()) {
            throw new BadRequestException("Queue entry already exists for this visit");
        }

        LocalDate today = visit.getVisitDate();
        Integer max = queueRepository.findMaxQueueNumber(
                visit.getDoctor().getId(), today);

        VisitQueue queue = VisitQueue.builder()
                .visit(visit)
                .doctor(visit.getDoctor())
                .queueDate(today)
                .queueNumber(max + 1)
                .status(QueueStatus.WAITING)
                .build();

        visit.setStatus(VisitStatus.WAITING);
        visitRepository.save(visit);

        return QueueResponse.from(queueRepository.save(queue));
    }

    @Transactional(readOnly = true)
    public VisitResponse getVisit(Long id) {
        return VisitResponse.from(visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Visit not found: " + id)));
    }

    @Transactional(readOnly = true)
    public java.util.List<VisitResponse> patientHistory(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found: " + patientId);
        }
        return visitRepository.findByPatientIdOrderByVisitDateDescCreatedAtDesc(patientId)
                .stream().map(VisitResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public java.util.List<QueueResponse> doctorQueue(Long doctorId, LocalDate date) {
        return queueRepository.findByDoctorIdAndQueueDateOrderByQueueNumberAsc(
                        doctorId, date)
                .stream().map(QueueResponse::from).toList();
    }

    private String generateVisitNumber() {
        String number;
        do {
            number = "V-" +
                    LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) +
                    "-" +
                    UUID.randomUUID().toString()
                            .replace("-", "").substring(0, 6).toUpperCase();
        } while (visitRepository.existsByVisitNumber(number));
        return number;
    }

    @Transactional(readOnly = true)
    public List<QueueResponse> myDoctorQueue(
            String username,
            LocalDate date
    ) {
        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor profile not found for logged-in user"
                        ));

        return doctorQueue(doctor.getId(), date);
    }

    @Transactional(readOnly = true)
    public List<VisitResponse> getDoctorVisits(String username) {

        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor profile not found for logged-in user"
                        )
                );

        return visitRepository
                .findByDoctorIdOrderByVisitDateDescCreatedAtDesc(doctor.getId())
                .stream()
                .map(VisitResponse::from)
                .toList();
    }
}
