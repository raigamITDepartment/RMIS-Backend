package com.company.finance.finance_manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class InvoiceStatusAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;

    private String statusField; // "fgsStatus" or "territoryStatus"

    private String oldValue;

    private String newValue;

    private String updatedBy;

    private LocalDateTime updatedAt;

}
