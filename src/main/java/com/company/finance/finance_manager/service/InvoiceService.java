package com.company.finance.finance_manager.service;

import com.company.finance.finance_manager.dto.InvoiceDTO;
import com.company.finance.finance_manager.dto.InvoiceListDTO;
import com.company.finance.finance_manager.dto.InvoiceUpdateDTO;
import com.company.finance.finance_manager.enums.EInvoiceType;
import com.company.finance.finance_manager.enums.EStatus;
import com.company.finance.finance_manager.entity.Invoice;
import com.company.finance.finance_manager.exception.ResourceNotFoundException;
import com.company.finance.finance_manager.repository.InvoiceRepository;
import com.company.finance.finance_manager.repository.InvoiceStatusAuditRepository;
import com.company.finance.finance_manager.repository.UserRepository;
import com.company.finance.finance_manager.specification.InvoiceSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvoiceStatusAuditService invoiceStatusAuditService;

    @Autowired
    private InvoiceStatusAuditRepository auditRepository;

    private Map<String, String> parseSearchString(String search) {
        return Arrays.stream(search.split(","))
                .map(s -> s.split(":", 2))
                .filter(pair -> pair.length == 2)
                .collect(Collectors.toMap(pair -> pair[0].trim(), pair -> pair[1].trim()));
    }

    public Page<InvoiceListDTO> getInvoicesPaginated(Pageable pageable,
                                                     String search,
                                                     String location,
                                                     String fgsStatus,
                                                     String financeStatus,
                                                     String startDateStr,
                                                     String endDateStr
    ) {
        Map<String, String> filters = parseSearchString(search);

        // Add date filters to the map (optional)
        if (location != null) {
            filters.put("location", location);
        }
        if (fgsStatus != null) {
            filters.put("fgsStatus", fgsStatus);
        }
        if (financeStatus != null) {
            filters.put("financeStatus", financeStatus);
        }
        if (startDateStr != null) {
            filters.put("start_date", startDateStr);
        }
        if (endDateStr != null) {
            filters.put("end_date", endDateStr);
        }

        Specification<Invoice> spec = InvoiceSpecifications.withFilters(filters);
        Page<Invoice> invoicePage = invoiceRepository.findAll(spec, pageable);

        return invoicePage.map(InvoiceListDTO::new);
    }

    public Page<String> getInvoiceLocations(Pageable pageable, String search) {
        Map<String, String> filters = parseSearchString(search);
        String locationName = filters.getOrDefault("name", "");

        List<String> allLocations = invoiceRepository.findLocations(locationName);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allLocations.size());

        List<String> pagedLocations = allLocations.subList(start, end);
        return new PageImpl<>(pagedLocations, pageable, allLocations.size());
    }

    public Invoice getInvoiceById(Integer id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
    }

    public Invoice createInvoice(InvoiceDTO invoiceDTO) {
        Invoice invoice = new Invoice();

        invoice.setCompanyName(invoiceDTO.getCompanyName());
        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setValue(invoiceDTO.getValue());
        invoice.setFgsStatus(EStatus.PENDING);
        invoice.setFinanceStatus(EStatus.PENDING);

        return invoiceRepository.save(invoice);
    }

    public Invoice updateInvoice(Integer id, InvoiceUpdateDTO invoiceUpdateDTO) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));

        String currentUser = getCurrentUsername(); // Fetch from SecurityContext or Auth

        // FGS Status change
        if (invoiceUpdateDTO.getFgsStatus() != null && !Objects.equals(invoice.getFgsStatus(), invoiceUpdateDTO.getFgsStatus())) {
            invoiceStatusAuditService.saveStatusAudit(
                    invoice.getInvoiceNumber(),
                    "fgsStatus",
                    String.valueOf(invoice.getFgsStatus()),
                    String.valueOf(invoiceUpdateDTO.getFgsStatus()),
                    currentUser);

            invoice.setFgsStatus(invoiceUpdateDTO.getFgsStatus());
        }

        // Finance Status change
        if (invoiceUpdateDTO.getFinanceStatus() != null && !Objects.equals(invoice.getFinanceStatus(), invoiceUpdateDTO.getFinanceStatus())) {
            invoiceStatusAuditService.saveStatusAudit(
                    invoice.getInvoiceNumber(),
                    "financeStatus",
                    String.valueOf(invoice.getFinanceStatus()),
                    String.valueOf(invoiceUpdateDTO.getFinanceStatus()),
                    currentUser);

            invoice.setFinanceStatus(invoiceUpdateDTO.getFinanceStatus());
        }


        invoice.setRemarks(invoiceUpdateDTO.getRemarks());


        if (invoiceUpdateDTO.getInvoiceType() == null) {

            EInvoiceType invoiceType = invoice.getInvoiceType();

            if (invoiceType != null) {
                invoice.setInvoiceType(invoiceType); // Set the invoiceType
            } else {
                throw new ResourceNotFoundException("Invoice Type not found for the given ID.");
            }
        } else {
            invoice.setInvoiceType(invoiceUpdateDTO.getInvoiceType());
        }


        return invoiceRepository.save(invoice);
    }

    public Invoice partialUpdateInvoice(Integer id, InvoiceUpdateDTO invoiceUpdateDTO) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));

        String currentUser = getCurrentUsername(); // Fetch from SecurityContext or similar

        // Partial update: only update fields that are non-null in DTO

        if (invoiceUpdateDTO.getFgsStatus() != null && !Objects.equals(invoice.getFgsStatus(), invoiceUpdateDTO.getFgsStatus())) {
            invoiceStatusAuditService.saveStatusAudit(
                    invoice.getInvoiceNumber(),
                    "fgsStatus",
                    String.valueOf(invoice.getFgsStatus()),
                    String.valueOf(invoiceUpdateDTO.getFgsStatus()),
                    currentUser
            );
            invoice.setFgsStatus(invoiceUpdateDTO.getFgsStatus());
        }

        if (invoiceUpdateDTO.getFinanceStatus() != null && !Objects.equals(invoice.getFinanceStatus(), invoiceUpdateDTO.getFinanceStatus())) {
            invoiceStatusAuditService.saveStatusAudit(
                    invoice.getInvoiceNumber(),
                    "financeStatus",
                    String.valueOf(invoice.getFinanceStatus()),
                    String.valueOf(invoiceUpdateDTO.getFinanceStatus()),
                    currentUser
            );
            invoice.setFinanceStatus(invoiceUpdateDTO.getFinanceStatus());
        }

        if (invoiceUpdateDTO.getInvoiceType() != null) {
            invoice.setInvoiceType(invoiceUpdateDTO.getInvoiceType());
        }

        if (invoiceUpdateDTO.getRemarks() != null) {
            invoice.setRemarks(invoiceUpdateDTO.getRemarks());
        }

        return invoiceRepository.save(invoice);
    }

    private String getCurrentUsername() {
        // Example using Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "SYSTEM";
    }
}
