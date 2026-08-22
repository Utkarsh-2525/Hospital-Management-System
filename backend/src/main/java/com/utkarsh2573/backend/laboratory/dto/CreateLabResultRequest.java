package com.utkarsh2573.backend.laboratory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateLabResultRequest(

        @NotBlank
        String result,

        @Size(max = 1000)
        String remarks,

        String attachmentUrl
) {
}