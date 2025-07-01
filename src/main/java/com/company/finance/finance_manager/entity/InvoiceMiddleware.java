package com.company.finance.finance_manager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class InvoiceMiddleware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String docNumber;

    private LocalDateTime docDate;

    private String cardCode;

    private Double docTotal;

    private String wareHouse;

    private String userSign;

    private LocalDateTime syncedAt;

    private String CompanyName;
}
