package com.company.finance.finance_manager.service;

import com.company.finance.finance_manager.entity.InvoiceStatusAudit;
import com.company.finance.finance_manager.repository.InvoiceStatusAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceStatusAuditService {

    @Autowired
    private InvoiceStatusAuditRepository auditRepository;

    public Page<InvoiceStatusAudit> getAllInvoices(Pageable pageable) {
        return auditRepository.findAll(pageable);
    }

    public void saveStatusAudit(String invoiceNumber,
                                String statusField,
                                String oldValue,
                                String newValue,
                                String user) {
        InvoiceStatusAudit audit = new InvoiceStatusAudit();
        audit.setInvoiceNumber(invoiceNumber);
        audit.setStatusField(statusField);
        audit.setOldValue(oldValue);
        audit.setNewValue(newValue);
        audit.setUpdatedBy(user);
        audit.setUpdatedAt(LocalDateTime.now());
        auditRepository.save(audit);
    }

}
