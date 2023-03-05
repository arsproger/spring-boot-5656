package com.arsen.controllers.advice;

import com.arsen.exceptions.PaymentErrorResponse;
import com.arsen.exceptions.PaymentNotFoundException;
import com.arsen.exceptions.PersonErrorResponse;
import com.arsen.exceptions.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final PersonErrorResponse personErrorResponse;
    private final PaymentErrorResponse paymentErrorResponse;

    @Autowired
    public GlobalExceptionHandler(PersonErrorResponse personErrorResponse, PaymentErrorResponse paymentErrorResponse) {
        this.personErrorResponse = personErrorResponse;
        this.paymentErrorResponse = paymentErrorResponse;
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        personErrorResponse.setMessage("Человек с таким id не был найден!");
        personErrorResponse.setDateTime(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_XML)
                .body(personErrorResponse);
    }

    @ExceptionHandler
    private ResponseEntity<PaymentErrorResponse> handleException(PaymentNotFoundException e) {
        paymentErrorResponse.setMessage("Платеж с таким id не был найден!");
        paymentErrorResponse.setDateTime(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_XML)
                .body(paymentErrorResponse);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_XML)
                .body("Bad request!");
    }
}
