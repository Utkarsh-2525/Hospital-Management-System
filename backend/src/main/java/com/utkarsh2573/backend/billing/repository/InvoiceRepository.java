package com.utkarsh2573.backend.billing.repository;

import com.utkarsh2573.backend.billing.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    Optional<Invoice> findByVisitId(Long visitId);
    List<Invoice> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    boolean existsByInvoiceNumber(String invoiceNumber);
}
