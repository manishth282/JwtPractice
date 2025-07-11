package com.company.projectjwt1.exceptions;

import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //    @ResponseBody
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Map<String, String>> runtimeException(Exception exception) {
//        logger.warn("************** GlobalExceptionHandler's method-> runtimeException");
//        Map<String, String> response = new HashMap<>();
//        response.put("error", exception.getMessage());
//
//        return ResponseEntity.badRequest().body(response);
//    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> usernameNotFoundException(Exception exception){
        logger.warn("************** GlobalExceptionHandler's method -> usernameNotFoundException");
        Map<String, String> response = new HashMap<>();
        response.put("error", exception.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * This exception handler for handle exception when Client not got register while trying to register
     */
    @ExceptionHandler(ClientNotRegisterException.class)
    public ResponseEntity<Map<String, String>> clientNotRegisterException(Exception exception) {
        logger.warn("************** GlobalExceptionHandler's method -> clientNotRegisterException");
        Map<String, String> response = new HashMap<>();
        response.put("error", exception.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
        logger.warn("************** GlobalExceptionHandler's method ->handlerValidationException");
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }


}
