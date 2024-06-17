package com.imanzi.marketplace.util.exception;

import com.imanzi.marketplace.util.ReferencedWarning;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.CONFLICT)
public class ReferencedException extends RuntimeException {

    public ReferencedException() {
        super();
    }

    public ReferencedException(final ReferencedWarning referencedWarning) {
        super(referencedWarning.toMessage());
    }

}
