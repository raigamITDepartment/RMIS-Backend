package com.company.finance.finance_manager.dto;

import com.company.finance.finance_manager.enums.ERequestType;
import com.company.finance.finance_manager.enums.EStatus;
import com.company.finance.finance_manager.entity.Request;
import lombok.Data;

@Data
public class RequestPaginatedDTO {
    private int id;

    private String companyName;

    private String invoiceNumber;

    private ERequestType requestType;

    private EStatus status;

    public RequestPaginatedDTO(Request request) {
        this.id = request.getId();
        this.companyName = request.getInvoice().getCompanyName();
        this.invoiceNumber = request.getInvoice().getInvoiceNumber();
        this.requestType = request.getRequestType();
        this.status = request.getStatus();
    }
}
