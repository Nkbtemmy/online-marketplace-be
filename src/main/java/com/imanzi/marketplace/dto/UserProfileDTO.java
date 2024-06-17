package com.imanzi.marketplace.dto;

import com.imanzi.marketplace.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;

}
