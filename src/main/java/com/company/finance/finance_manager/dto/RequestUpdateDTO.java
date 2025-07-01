package com.company.finance.finance_manager.dto;

import com.company.finance.finance_manager.enums.ERequestType;
import com.company.finance.finance_manager.enums.EResponse;
import lombok.Data;

@Data
public class RequestUpdateDTO {
    private Integer id;

    private Integer invoiceId;

    private ERequestType requestType;

    private EResponse response;

}
