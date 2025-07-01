package com.company.finance.finance_manager.repository;

import com.company.finance.finance_manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    boolean existsByEmail(String slug);

}
