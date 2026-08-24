package com.utkarsh2573.backend.laboratory.service;

import com.utkarsh2573.backend.common.enums.LabOrderStatus;
import com.utkarsh2573.backend.consultation.entity.Consultation;
import com.utkarsh2573.backend.consultation.repository.ConsultationRepository;
import com.utkarsh2573.backend.exception.BadRequestException;
import com.utkarsh2573.backend.exception.ResourceNotFoundException;
import com.utkarsh2573.backend.laboratory.dto.CreateLabOrderRequest;
import com.utkarsh2573.backend.laboratory.dto.LabOrderResponse;
import com.utkarsh2573.backend.laboratory.entity.LabOrder;
import com.utkarsh2573.backend.laboratory.entity.LabOrderItem;
import com.utkarsh2573.backend.laboratory.entity.LabTest;
import com.utkarsh2573.backend.laboratory.repository.LabOrderRepository;
import com.utkarsh2573.backend.laboratory.repository.LabTestRepository;
import com.utkarsh2573.backend.patient.entity.Patient;
import com.utkarsh2573.backend.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.context.annotation.ConfigurationClassUtils.getOrder;

@Service
@RequiredArgsConstructor
public class LabOrderService {

    private final LabOrderRepository labOrderRepository;
    private final PatientRepository patientRepository;
    private final ConsultationRepository consultationRepository;
    private final LabTestRepository labTestRepository;

    @Transactional
    public LabOrderResponse create(CreateLabOrderRequest request) {

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found: " + request.patientId()
                        )
                );

        Consultation consultation =
                consultationRepository.findById(request.consultationId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Consultation not found: "
                                                + request.consultationId()
                                )
                        );

        if (!consultation.getVisit()
                .getPatient()
                .getId()
                .equals(patient.getId())) {

            throw new BadRequestException(
                    "Consultation does not belong to patient"
            );
        }

        List<LabTest> labTests = labTestRepository
                .findAllById(request.labTestIds());

        if (labTests.size() != request.labTestIds().size()) {
            throw new ResourceNotFoundException(
                    "One or more lab tests not found"
            );
        }

        for (LabTest labTest : labTests) {

            if (!labTest.isActive()) {
                throw new BadRequestException(
                        "Lab test is inactive: " + labTest.getTestCode()
                );
            }
        }

        LabOrder order = LabOrder.builder()
                .orderNumber(generateOrderNumber())
                .patient(patient)
                .consultation(consultation)
                .instructions(request.instructions())
                .build();

        for (LabTest labTest : labTests) {

            LabOrderItem item = LabOrderItem.builder()
                    .labTest(labTest)
                    .build();

            order.addItem(item);
        }

        return LabOrderResponse.from(
                labOrderRepository.save(order)
        );
    }

    @Transactional(readOnly = true)
    public List<LabOrderResponse> getAll() {

        return labOrderRepository.findAll()
                .stream()
                .map(LabOrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public LabOrderResponse getById(Long id) {

        LabOrder order = labOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab order not found: " + id
                        )
                );

        return LabOrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<LabOrderResponse> getByPatient(Long patientId) {

        return labOrderRepository
                .findByPatientIdOrderByOrderedAtDesc(patientId)
                .stream()
                .map(LabOrderResponse::from)
                .toList();
    }

    private String generateOrderNumber() {

        String number;

        do {
            number = "LAB-" +
                    UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .substring(0, 10)
                            .toUpperCase();

        } while (labOrderRepository.existsByOrderNumber(number));

        return number;
    }

    @Transactional
    public LabOrderResponse collectSample(Long orderId) {

        LabOrder order = getOrder(orderId);

        if (order.getStatus() != LabOrderStatus.ORDERED) {
            throw new BadRequestException(
                    "Sample can only be collected for an ORDERED lab order"
            );
        }

        order.setStatus(LabOrderStatus.SAMPLE_COLLECTED);
        order.setSampleCollectedAt(LocalDateTime.now());

        return LabOrderResponse.from(
                labOrderRepository.save(order)
        );
    }

    @Transactional
    public LabOrderResponse startProcessing(Long orderId) {

        LabOrder order = getOrder(orderId);

        if (order.getStatus() != LabOrderStatus.SAMPLE_COLLECTED) {
            throw new BadRequestException(
                    "Processing can only start after sample collection"
            );
        }

        order.setStatus(LabOrderStatus.PROCESSING);

        return LabOrderResponse.from(
                labOrderRepository.save(order)
        );
    }

    @Transactional
    public LabOrderResponse complete(Long orderId) {

        LabOrder order = getOrder(orderId);

        if (order.getStatus() != LabOrderStatus.PROCESSING) {
            throw new BadRequestException(
                    "Lab order can only be completed while PROCESSING"
            );
        }

        order.setStatus(LabOrderStatus.COMPLETED);
        order.setCompletedAt(LocalDateTime.now());

        return LabOrderResponse.from(
                labOrderRepository.save(order)
        );
    }

    private LabOrder getOrder(Long orderId) {

        return labOrderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab order not found: " + orderId
                        )
                );
    }
}