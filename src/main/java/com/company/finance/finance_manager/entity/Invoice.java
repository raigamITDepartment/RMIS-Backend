package com.company.finance.finance_manager.entity;

import com.company.finance.finance_manager.enums.EInvoiceType;
import com.company.finance.finance_manager.enums.EStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Invoice extends TimeAuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String companyName;

    private String invoiceNumber; // Todo -> DocNumber

    private Double value; // Todo -> DocTotal

    @Enumerated(EnumType.STRING)
    private EStatus fgsStatus = EStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private EStatus financeStatus = EStatus.PENDING;

    private String territory;  // Todo -> CardCode

    private String remarks;

    private String location;

    private String createdUser;

    @Enumerated(EnumType.STRING)
    private EInvoiceType invoiceType = EInvoiceType.AGENCY;

//    private LocalDateTime DocDate;

}
