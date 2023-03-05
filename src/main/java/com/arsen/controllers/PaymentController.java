package com.arsen.controllers;

import com.arsen.dto.PaymentHistoryDTO;
import com.arsen.exceptions.PaymentErrorResponse;
import com.arsen.models.PaymentHistory;
import com.arsen.models.Person;
import com.arsen.services.PaymentHistoryService;
import com.arsen.services.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
