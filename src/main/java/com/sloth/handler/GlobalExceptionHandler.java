package com.sloth.handler;

import lombok.extern.slf4j.Slf4j;
import com.sloth.exception.BusinessException;
import com.sloth.exception.EntityNotFoundException;
import com.sloth.exception.EntityValidException;
import com.sloth.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
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
        ErrorMessage em = ErrorMessage.builder()
                .errorMessage(e.getMessage())
                .status(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .referedUrl(request.getRequestURL().toString())
                .build()
                ;
        return new ResponseEntity<>(em, HttpStatus.BAD_REQUEST);
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

        ErrorMessage em = ErrorMessage.builder()
                .errorMessage(sb.toString())
                .status(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .referedUrl(request.getRequestURL().toString())
                .build()
                ;
        return new ResponseEntity<>(em, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorMessage> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        ErrorMessage em = ErrorMessage.builder()
                .errorMessage(e.getMessage())
                .status(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .referedUrl(request.getRequestURL().toString())
                .build()
                ;
        return new ResponseEntity<>(em, HttpStatus.BAD_REQUEST);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorMessage> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        ErrorMessage em = ErrorMessage.builder()
                .errorMessage(e.getMessage())
                .status(false)
                .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                .referedUrl(request.getRequestURL().toString())
                .build()
                ;
        return new ResponseEntity<>(em, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.error("handleAccessDeniedException", e);
        ErrorMessage em = ErrorMessage.builder()
                .errorMessage(e.getMessage())
                .status(false)
                .code(HttpStatus.FORBIDDEN.value())
                .referedUrl(request.getRequestURL().toString())
                .build()
                ;
        return new ResponseEntity<>(em, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = { BusinessException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    protected ResponseEntity<ErrorMessage> handleConflict(BusinessException e, HttpServletRequest request) {
        log.error("GlobalException", e);
        System.out.println(e.getMessage());
        ErrorMessage em = ErrorMessage.builder()
                .errorMessage(e.getMessage())
                .status(false)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .referedUrl(request.getRequestURL().toString())
                .build()
                ;
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(em);
    }

    @ExceptionHandler({ EntityValidException.class })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    protected ResponseEntity<?> handleEntityValidException(EntityValidException e, HttpServletRequest request) {
        List<String> messages = e.getEntities().stream()
                .map((entityName) -> entityName + " is required")
                .collect(Collectors.toList());

        ErrorMessage em = ErrorMessage.builder()
                .status(false)
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .referedUrl(request.getRequestURL().toString())
                .result(messages)
                .build();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(em);
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    protected ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        ErrorMessage em = ErrorMessage.builder()
                .status(false)
                .code(HttpStatus.NOT_FOUND.value())
                .referedUrl(request.getRequestURL().toString())
                .result(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(em);
    }

}