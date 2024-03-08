package com.baseline.common;

import com.baseline.common.errorcode.CommonErrorCode;
import com.baseline.common.exception.BadRequestException;
import com.baseline.common.exception.BaseException;
import com.baseline.common.exception.InternalServerException;
import com.baseline.common.result.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResult> handleBaseException(HttpServletRequest request, BaseException e) {
        return new ResponseEntity<>(e.toBaseResult(), e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResult handleMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException e) {
        FieldError fieldError = e.getFieldError();
        return new BadRequestException(
                CommonErrorCode.INVALID_PARAMETER,
                fieldError == null ? "" : fieldError.getField()
        ).toBaseResult();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public BaseResult handleDataIntegrityViolation(HttpServletRequest request, DataIntegrityViolationException e) {
        return new BaseResult(
                CommonErrorCode.ALREADY_EXIST.getErrorCode(),
                CommonErrorCode.ALREADY_EXIST.getErrorMessage()
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResult handleNotFoundPath(HttpServletRequest request, NoHandlerFoundException e) {
        return new BadRequestException("handlerNotFound",
                "Request URL({0}) is not found.",
                e.getRequestURL()).toBaseResult();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResult handleMissingRequestParams(MissingServletRequestParameterException e) {
        return new BadRequestException(CommonErrorCode.INVALID_PARAMETER, e.getParameterName()).toBaseResult();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResult handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        return new BadRequestException(CommonErrorCode.INVALID_PARAMETER, e.getParameter().getParameterName()).toBaseResult();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResult handleException(Exception e) {
        log.error("Internal Server Exception", e);
        return new InternalServerException(CommonErrorCode.INTERNAL_SERVER_EXCEPTION).toBaseResult();
    }
}
