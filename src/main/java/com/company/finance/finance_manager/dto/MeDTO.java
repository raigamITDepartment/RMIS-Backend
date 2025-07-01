package com.company.finance.finance_manager.dto;

import com.company.finance.finance_manager.enums.ERole;
import lombok.Data;

import java.util.Set;

@Data
public class MeDTO {

    private String firstName;

    private String lastName;

    private String email;

    private Set<ERole> roles;

    public MeDTO(String firstName, String lastName, String email, Set<ERole> roles ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }
}
