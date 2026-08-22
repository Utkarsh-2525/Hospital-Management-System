package com.utkarsh2573.backend.laboratory.service;

import com.utkarsh2573.backend.exception.BadRequestException;
import com.utkarsh2573.backend.exception.ResourceNotFoundException;
import com.utkarsh2573.backend.laboratory.dto.CreateLabResultRequest;
import com.utkarsh2573.backend.laboratory.dto.LabResultResponse;
import com.utkarsh2573.backend.laboratory.entity.LabOrder;
import com.utkarsh2573.backend.laboratory.entity.LabResult;
import com.utkarsh2573.backend.laboratory.repository.LabOrderRepository;
import com.utkarsh2573.backend.laboratory.repository.LabResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LabResultService {

    private final LabOrderRepository labOrderRepository;
    private final LabResultRepository labResultRepository;

    @Transactional
    public LabResultResponse create(
            Long orderId,
            CreateLabResultRequest request
    ) {

        LabOrder order = labOrderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab order not found: " + orderId
                        )
                );

        if (labResultRepository.findByLabOrderId(orderId).isPresent()) {
            throw new BadRequestException(
                    "Lab result already exists for this order"
            );
        }

        LabResult result = LabResult.builder()
                .labOrder(order)
                .result(request.result())
                .remarks(request.remarks())
                .attachmentUrl(request.attachmentUrl())
                .reportedAt(LocalDateTime.now())
                .build();

        return LabResultResponse.from(
                labResultRepository.save(result)
        );
    }

    @Transactional(readOnly = true)
    public LabResultResponse getByOrderId(Long orderId) {

        LabResult result = labResultRepository
                .findByLabOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab result not found for order: "
                                        + orderId
                        )
                );

        return LabResultResponse.from(result);
    }
}