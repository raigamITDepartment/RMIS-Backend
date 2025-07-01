package com.company.finance.finance_manager.repository;

import com.company.finance.finance_manager.entity.InvoiceMiddleware;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceMiddlewareRepository extends JpaRepository<InvoiceMiddleware, Integer> {
    List<InvoiceMiddleware> findBySyncedAtIsNull();

}
