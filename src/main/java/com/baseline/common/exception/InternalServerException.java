package com.baseline.common.exception;

import com.baseline.common.errorcode.BaseErrorCode;
import com.baseline.common.errorcode.CommonErrorCode;
import org.springframework.http.HttpStatus;

public class InternalServerException extends BaseException {
    public InternalServerException(String errorMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, CommonErrorCode.INTERNAL_SERVER_EXCEPTION.getErrorCode(), errorMessage);
    }
    public InternalServerException(BaseErrorCode baseErrorCode) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, baseErrorCode);
    }
}
