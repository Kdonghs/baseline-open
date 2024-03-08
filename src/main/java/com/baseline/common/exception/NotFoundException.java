package com.baseline.common.exception;

import com.baseline.common.errorcode.BaseErrorCode;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(BaseErrorCode errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode);
    }
}
