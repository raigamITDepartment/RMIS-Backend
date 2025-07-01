package com.company.finance.finance_manager.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ErrorResponse {
    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

}

