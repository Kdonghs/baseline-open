package com.baseline.common.exception;

import com.baseline.common.errorcode.BaseErrorCode;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

public class BadRequestException extends BaseException {
    public BadRequestException(BaseErrorCode baseErrorCode) {
        super(HttpStatus.BAD_REQUEST, baseErrorCode);
    }

    public BadRequestException(BaseErrorCode baseErrorCode, Object... args) {
        super(HttpStatus.BAD_REQUEST, baseErrorCode.getErrorCode(), MessageFormat.format(baseErrorCode.getErrorMessage(), args));
    }

    public BadRequestException(String errorCode, String errorMessage, Object... args) {
        super(HttpStatus.BAD_REQUEST, errorCode, MessageFormat.format(errorMessage, args));
    }
}
