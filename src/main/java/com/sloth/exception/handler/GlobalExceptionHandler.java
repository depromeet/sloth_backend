package com.sloth.exception.handler;

import com.sloth.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("handleMethodArgumentNotValidException", e);
        return exceptionResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    /**
     * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorMessage> handleBindException(BindException e, HttpServletRequest request) {
        log.error("handleBindException", e);

        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        if(bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage()).append("\n");
            }
        }

        return exceptionResponseEntity(sb.toString(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorMessage> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        return exceptionResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorMessage> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        return exceptionResponseEntity(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED, request.getRequestURI());
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.error("handleAccessDeniedException", e);
        return exceptionResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI());
    }

    @ExceptionHandler(value = { BusinessException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorMessage> handleConflict(BusinessException e, HttpServletRequest request) {
        log.error("GlobalException", e);
        System.out.println(e.getMessage());
        return exceptionResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }

    @ExceptionHandler({ EntityValidException.class })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<?> handleEntityValidException(EntityValidException e, HttpServletRequest request) {
        List<String> messageList = e.getEntities().stream()
                .map((entityName) -> entityName + " is required")
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        for (String message : messageList) {
            sb.append(message);
        }
        String messages = sb.toString();

        return exceptionResponseEntity(messages, HttpStatus.UNPROCESSABLE_ENTITY, request.getRequestURI());
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        return exceptionResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
    }

    @ExceptionHandler(MemberTokenNotFoundException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorMessage> memberNotFoundExceptionHandle(MemberTokenNotFoundException e, HttpServletRequest request) {
        return exceptionResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI());
    }

    @ExceptionHandler(FeignClientException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorMessage> memberNotFoundExceptionHandle(FeignClientException e, HttpServletRequest request) {
        return exceptionResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI());
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> invalidParameterExceptionHandle(InvalidParameterException e, HttpServletRequest request) {
        return exceptionResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(NeedEmailConfirmException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorMessage> needEmailConfirmExceptionHandle(NeedEmailConfirmException e, HttpServletRequest request) {
        return exceptionResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorMessage> forbiddenExceptionHandle(ForbiddenException e, HttpServletRequest request) {
        return exceptionResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorMessage> handleException(Exception e, HttpServletRequest request) {
        return exceptionResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }

    private ResponseEntity<ErrorMessage> exceptionResponseEntity(String message, HttpStatus status, String requestURI) {
        ErrorMessage em = ErrorMessage.builder()
                .errorMessage(message)
                .code(status.value())
                .referedUrl(requestURI)
                .build();
        return ResponseEntity.status(status).body(em);
    }
}