package com.baseline.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberErrorCode implements BaseErrorCode {
    ALREADY_EXIST("20000", "member already exists."),
    INVALID_ID_TOKEN("20001", "invalid id token."),
    NOT_FOUND("20002", "Member does not exist."),
    ;

    private final String errorCode;
    private final String errorMessage;

}
