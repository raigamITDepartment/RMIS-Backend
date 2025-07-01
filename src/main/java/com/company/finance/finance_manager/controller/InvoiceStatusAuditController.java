package com.company.finance.finance_manager.controller;

import com.company.finance.finance_manager.dto.PaginatedResponse;
import com.company.finance.finance_manager.entity.InvoiceStatusAudit;
import com.company.finance.finance_manager.service.InvoiceStatusAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoice-status-audits")
public class InvoiceStatusAuditController {

    @Autowired
    private InvoiceStatusAuditService auditService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<InvoiceStatusAudit>> getAllInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<InvoiceStatusAudit> invoiceStatusAudits = auditService.getAllInvoices(pageable);

        PaginatedResponse<InvoiceStatusAudit> response = new PaginatedResponse<>(invoiceStatusAudits);
        return ResponseEntity.ok(response);
    }
}
