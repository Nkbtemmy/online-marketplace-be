package com.imanzi.marketplace.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class UserException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public UserException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

//    @Override
//    public String getMessage() {
//        return errorMessage;
//    }
}
