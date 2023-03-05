package com.arsen.controllers;

import com.arsen.dto.PaymentHistoryDTO;
import com.arsen.exceptions.PaymentErrorResponse;
import com.arsen.exceptions.PaymentNotFoundException;
import com.arsen.models.PaymentHistory;
import com.arsen.models.Person;
import com.arsen.services.PaymentHistoryService;
import com.arsen.services.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pay")
@Slf4j
public class PaymentController {
    private final ModelMapper modelMapper;
    private final PaymentHistoryService paymentHistoryService;
    private final PersonService personService;
    private final PaymentErrorResponse paymentErrorResponse;

    @Autowired
    public PaymentController(ModelMapper modelMapper, PaymentHistoryService paymentHistoryService,
                             PersonService personService, PaymentErrorResponse paymentErrorResponse) {
        this.modelMapper = modelMapper;
        this.paymentHistoryService = paymentHistoryService;
        this.personService = personService;
        this.paymentErrorResponse = paymentErrorResponse;
    }

    @GetMapping("/all")
    public List<PaymentHistoryDTO> findAll() {
        log.info("Payment controller: findAll starting!");

        return paymentHistoryService.findAll().stream().map(
                this::convertToPaymentHistoryDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PaymentHistoryDTO findById(@PathVariable Long id) {
        log.info("Payment controller: findById starting!");

        return convertToPaymentHistoryDTO(paymentHistoryService.findById(id));
    }

    @GetMapping("/{id}/{amount}")
    public Boolean payTransfer(@PathVariable Long id,
                               @PathVariable Double amount) {
        log.info("Payment controller: payTransfer starting!");

        Person person = personService.findById(id);
        if (person.getBalance() - amount < 0)
            return false;

        person.setBalance(person.getBalance() - amount);
        paymentHistoryService.save(new PaymentHistory(amount, person));

        return true;
    }

    private PaymentHistoryDTO convertToPaymentHistoryDTO(PaymentHistory paymentHistory) {
        return modelMapper.map(paymentHistory, PaymentHistoryDTO.class);
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
    private ResponseEntity<String> response(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_XML)
                .body("Bad request!");
    }
}
