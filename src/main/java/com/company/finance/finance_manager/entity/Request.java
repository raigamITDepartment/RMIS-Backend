package com.company.finance.finance_manager.entity;

import com.company.finance.finance_manager.enums.ERequestType;
import com.company.finance.finance_manager.enums.EStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request extends TimeAuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Enumerated(EnumType.STRING)
    private ERequestType requestType;

    @Enumerated(EnumType.STRING)
    private EStatus status;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User created_by;

    @ManyToOne
    @JoinColumn(name = "accepted_by")
    private User accepted_by;

}
