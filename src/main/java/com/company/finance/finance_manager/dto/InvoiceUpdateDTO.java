package com.company.finance.finance_manager.dto;

import com.company.finance.finance_manager.enums.EInvoiceType;
import com.company.finance.finance_manager.enums.EStatus;
import lombok.Data;

@Data
public class InvoiceUpdateDTO {
    private EStatus fgsStatus;

    private EStatus financeStatus;

    private String remarks;

    private EInvoiceType invoiceType;
}
