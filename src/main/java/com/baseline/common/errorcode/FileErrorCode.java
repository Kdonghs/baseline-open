package com.baseline.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileErrorCode implements BaseErrorCode {
    NOT_FOUND("60001", "File not found."),
    ALREADY_EXIST("60002", "File already exists."),
    EMPTY_FILE("60003", "Provided file is empty."),
    SIZE_EXCEEDED("60004", "Size of the provided file exceeded the limit."),
    UPLOAD_FAILED("60005", "Upload failed"),
    WRONG_FILE_OWNER("60006", "Requesting user is not the owner of the request file."),
    ;

    private final String errorCode;
    private final String errorMessage;
}
