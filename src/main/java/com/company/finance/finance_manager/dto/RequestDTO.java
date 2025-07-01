package com.company.finance.finance_manager.dto;

import com.company.finance.finance_manager.enums.ERequestType;
import com.company.finance.finance_manager.enums.EStatus;
import lombok.Data;

@Data
public class RequestDTO {
    private Integer id;

    private Integer invoiceId;

    private ERequestType requestType;

    private EStatus status;

}
