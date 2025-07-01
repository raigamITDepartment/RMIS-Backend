package com.company.finance.finance_manager.dto;

import com.company.finance.finance_manager.enums.EInvoiceType;
import com.company.finance.finance_manager.enums.EStatus;
import com.company.finance.finance_manager.entity.Invoice;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceListDTO {

    private int id;

    private String companyName;

    private String invoiceNumber;

    private Double value;

    private EStatus fgsStatus;

    private EStatus financeStatus;

    private String territory;

    private String location;

    private String createdUser;

    private LocalDateTime createdAt;

    private EInvoiceType invoiceType;

    public InvoiceListDTO(Invoice invoice) {
        this.id = invoice.getId();
        this.companyName = invoice.getCompanyName();
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.value = invoice.getValue();
        this.fgsStatus = invoice.getFgsStatus();
        this.financeStatus = invoice.getFinanceStatus();
        this.territory = invoice.getTerritory();
        this.location = invoice.getLocation();
        this.createdUser = invoice.getCreatedUser();
        this.createdAt = invoice.getCreatedAt();
        this.invoiceType = invoice.getInvoiceType();
    }
}
