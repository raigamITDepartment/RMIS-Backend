package com.company.finance.finance_manager.dto;

import com.company.finance.finance_manager.enums.ERequestType;
import com.company.finance.finance_manager.entity.Request;
import lombok.Data;

@Data
public class RequestPagedDataDTO {
    private Integer id;

    private Integer invoiceId;

    private String invoiceNumber;

    private ERequestType requestType;

    public RequestPagedDataDTO(Request request) {
        this.id = request.getId();
        this.invoiceId = request.getInvoice().getId();
        this.invoiceNumber = request.getInvoice().getInvoiceNumber();
        this.requestType = request.getRequestType();
    }
}
