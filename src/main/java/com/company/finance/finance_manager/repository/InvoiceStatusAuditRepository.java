package com.company.finance.finance_manager.repository;

import com.company.finance.finance_manager.entity.InvoiceStatusAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceStatusAuditRepository extends JpaRepository<InvoiceStatusAudit, Long> {
}
