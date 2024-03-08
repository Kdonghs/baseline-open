package com.baseline.common.exception;

import com.baseline.common.errorcode.BaseErrorCode;
import com.baseline.common.result.BaseResult;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
public class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    public BaseException(HttpStatus httpStatus, BaseErrorCode baseErrorCode) {
        super(baseErrorCode.getErrorMessage());
        this.errorCode = baseErrorCode.getErrorCode();
        this.errorMessage = baseErrorCode.getErrorMessage();
        this.httpStatus = httpStatus;
    }

    public BaseException(HttpStatus httpStatus, String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

    public BaseResult toBaseResult() {
        return new BaseResult(errorCode, errorMessage);
    }
}
