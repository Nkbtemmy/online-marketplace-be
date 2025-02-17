package com.imanzi.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imanzi.marketplace.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
    private String token;
    private User data;
    private String errorMessage;
    private String message;
}
