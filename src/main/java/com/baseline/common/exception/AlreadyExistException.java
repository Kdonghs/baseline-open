package com.baseline.common.exception;

import com.baseline.common.errorcode.BaseErrorCode;
import org.springframework.http.HttpStatus;

public class AlreadyExistException extends BaseException {
    public AlreadyExistException(BaseErrorCode baseErrorCode) {
        super(HttpStatus.CONFLICT, baseErrorCode);
    }
}
