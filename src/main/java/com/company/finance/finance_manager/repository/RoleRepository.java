package com.company.finance.finance_manager.repository;


import com.company.finance.finance_manager.enums.ERole;
import com.company.finance.finance_manager.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

    boolean existsByName(ERole roleAdmin);
}
