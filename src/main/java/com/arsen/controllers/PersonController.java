package com.arsen.controllers;

import com.arsen.dto.PersonDTO;
import com.arsen.exceptions.PersonErrorResponse;
import com.arsen.models.Person;
import com.arsen.services.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public PersonController(ModelMapper modelMapper, PersonService personService,
                            PersonErrorResponse personErrorResponse) {
        this.modelMapper = modelMapper;
        this.personService = personService;
        this.personErrorResponse = personErrorResponse;
    }

    @GetMapping
    public String main() {
        return "Hello World!";
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

}
