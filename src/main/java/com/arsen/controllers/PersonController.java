package com.arsen.controllers;

import com.arsen.dto.PersonDTO;
import com.arsen.exceptions.PersonErrorResponse;
import com.arsen.exceptions.PersonNotFoundException;
import com.arsen.models.Person;
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
@RequestMapping("/person")
@Slf4j
public class PersonController {
    private final ModelMapper modelMapper;
    private final PersonService personService;
    private final PersonErrorResponse personErrorResponse;

    @Autowired
    public PersonController(ModelMapper modelMapper, PersonService personService, PersonErrorResponse personErrorResponse) {
        this.modelMapper = modelMapper;
        this.personService = personService;
        this.personErrorResponse = personErrorResponse;
    }

    @GetMapping("/all")
    public List<PersonDTO> getAll() {
        log.info("Person controller: getAll starting!");

        return personService.findAll().stream().map(
                this::convertToPersonDTO).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public PersonDTO getPerson(@PathVariable Long id) {
        log.info("Person controller: getPerson starting!");

        return convertToPersonDTO(personService.findById(id));
    }

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_XML_VALUE)
    public PersonDTO save(@RequestBody Person person) {
        log.info("Person controller: save starting!");

        return convertToPersonDTO(personService.findById(personService.save(person)));
    }

    @GetMapping("/check/{id}")
    public Boolean check(@PathVariable Long id) {
        log.info("Person controller: check starting!");

        return personService.findById(id).getActive();
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
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
    private ResponseEntity<String> response(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_XML)
                .body("Bad request!");
    }

}
