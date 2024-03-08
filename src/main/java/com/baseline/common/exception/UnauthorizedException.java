package com.baseline.common.exception;

import com.baseline.common.errorcode.BaseErrorCode;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(BaseErrorCode baseErrorCode) {
        super(HttpStatus.UNAUTHORIZED, baseErrorCode);
    }
}
