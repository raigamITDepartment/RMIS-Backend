package com.company.finance.finance_manager.dto;

import com.company.finance.finance_manager.enums.ERole;
import lombok.Data;

import java.util.Set;

@Data
public class UserPageDataDTO {
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private Set<ERole> roles;

    public UserPageDataDTO(Integer id,
                           String firstName,
                           String lastName,
                           String email,
                           String username,
                           Set<ERole> roles
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }

}
