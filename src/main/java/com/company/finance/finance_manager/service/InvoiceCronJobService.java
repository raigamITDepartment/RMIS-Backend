package com.company.finance.finance_manager.service;

import com.company.finance.finance_manager.entity.Invoice;
import com.company.finance.finance_manager.entity.InvoiceMiddleware;
import com.company.finance.finance_manager.enums.EInvoiceType;
import com.company.finance.finance_manager.enums.EStatus;
import com.company.finance.finance_manager.repository.InvoiceMiddlewareRepository;
import com.company.finance.finance_manager.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceCronJobService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMiddlewareRepository middlewareRepository;

    public InvoiceCronJobService(InvoiceRepository invoiceRepository, InvoiceMiddlewareRepository middlewareRepository) {
        this.invoiceRepository = invoiceRepository;
        this.middlewareRepository = middlewareRepository;
    }

    // Run at midnight every day
//    @Scheduled(cron = "0 0 0 * * *") // Seconds, Minutes, Hours, Day, Month, Day of Week
    @Scheduled(cron = "0 * * * * *") // every minute
    @Transactional
    public void migrateInvoiceData() {
        // Get only un-synced records
        List<InvoiceMiddleware> unsynced = middlewareRepository.findBySyncedAtIsNull();

        if (unsynced.isEmpty()) return;

        List<Invoice> invoices = unsynced.stream()
                .map(this::convertToInvoice)
                .collect(Collectors.toList());

        invoiceRepository.saveAll(invoices);

        // Mark middleware rows as synced
        unsynced.forEach(m -> m.setSyncedAt(LocalDateTime.now()));
        middlewareRepository.saveAll(unsynced);
    }

    private Invoice convertToInvoice(InvoiceMiddleware middleware) {
        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber(middleware.getDocNumber());
        invoice.setCreatedAt(middleware.getDocDate());
        invoice.setTerritory(middleware.getCardCode());
        invoice.setValue(middleware.getDocTotal());
        invoice.setLocation(middleware.getWareHouse());
        invoice.setCreatedUser(middleware.getUserSign());
        invoice.setCompanyName(middleware.getCompanyName());

        invoice.setFinanceStatus(EStatus.PENDING);
        invoice.setFgsStatus(EStatus.PENDING);
        invoice.setInvoiceType(EInvoiceType.AGENCY);

        return invoice;
    }
}
