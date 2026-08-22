package com.utkarsh2573.backend.billing.repository;

import com.utkarsh2573.backend.billing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByInvoiceIdOrderByPaidAtDesc(Long invoiceId);
}
