package com.utkarsh2573.backend.laboratory.service;

import com.utkarsh2573.backend.exception.BadRequestException;
import com.utkarsh2573.backend.exception.ResourceNotFoundException;
import com.utkarsh2573.backend.laboratory.dto.CreateLabResultRequest;
import com.utkarsh2573.backend.laboratory.dto.LabResultResponse;
import com.utkarsh2573.backend.laboratory.entity.LabOrderItem;
import com.utkarsh2573.backend.laboratory.entity.LabResult;
import com.utkarsh2573.backend.laboratory.repository.LabOrderItemRepository;
import com.utkarsh2573.backend.laboratory.repository.LabResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LabResultService {

    private final LabOrderItemRepository labOrderItemRepository;
    private final LabResultRepository labResultRepository;

    @Transactional
    public LabResultResponse create(
            Long orderItemId,
            CreateLabResultRequest request
    ) {

        LabOrderItem orderItem =
                labOrderItemRepository.findById(orderItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab order item not found: "
                                                + orderItemId
                                )
                        );

        if (labResultRepository
                .existsByLabOrderItemId(orderItemId)) {

            throw new BadRequestException(
                    "Lab result already exists for this test"
            );
        }

        LabResult result = LabResult.builder()
                .labOrderItem(orderItem)
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
    public LabResultResponse getByOrderItemId(
            Long orderItemId
    ) {

        LabResult result =
                labResultRepository
                        .findByLabOrderItemId(orderItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab result not found for order item: "
                                                + orderItemId
                                )
                        );

        return LabResultResponse.from(result);
    }
}