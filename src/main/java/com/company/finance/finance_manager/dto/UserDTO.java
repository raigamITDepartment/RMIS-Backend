package com.company.finance.finance_manager.dto;

import com.company.finance.finance_manager.enums.ERole;
import lombok.Data;

@Data
public class UserDTO {
    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private ERole role;

    private String password;

}
