package com.baseline.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements  BaseErrorCode {
    UNAUTHORIZED("10000", "Unauthorized."),
    INVALID_ID_TOKEN("10001", "Invalid id token."),
    INVALID_DATETIME_FORMAT("10002", "Invalid date time format."),
    INVALID_PARAMETER("10003", "Invalid parameter(`{0}`) value."),
    INTERNAL_SERVER_EXCEPTION("10004", "Internal server exception."),
    ALREADY_EXIST("10005", "Already exists.")
    ;

    private final String errorCode;
    private final String errorMessage;
}
