package com.company.finance.finance_manager.repository;

import com.company.finance.finance_manager.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>, JpaSpecificationExecutor<Invoice> {
    Page<Invoice> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);

    @Query("SELECT DISTINCT i.location FROM Invoice i WHERE i.location IS NOT NULL AND i.location <> '' AND LOWER(i.location) LIKE LOWER(CONCAT('%', :locationName, '%'))")
    List<String> findLocations(@Param("locationName") String locationName);
}
