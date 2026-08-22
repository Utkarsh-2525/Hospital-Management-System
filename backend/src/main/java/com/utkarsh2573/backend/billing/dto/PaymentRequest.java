package com.utkarsh2573.backend.billing.dto;

import com.utkarsh2573.backend.common.enums.PaymentMethod;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull Long invoiceId,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotNull PaymentMethod method,
        @Size(max = 100) String transactionReference
) {}
