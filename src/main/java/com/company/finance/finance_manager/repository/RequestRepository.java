package com.company.finance.finance_manager.repository;

import com.company.finance.finance_manager.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}
