package com.company.finance.finance_manager.dto;

import lombok.Data;

@Data
public class InvoiceDTO {
    private String companyName;

    private String invoiceNumber;

    private Double value;
}

