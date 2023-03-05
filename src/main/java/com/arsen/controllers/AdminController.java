package com.arsen.controllers;

import com.arsen.dto.PersonDTO;
import com.arsen.models.Person;
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
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final ModelMapper modelMapper;
    private final PersonService personService;

    @Autowired
    public AdminController(ModelMapper modelMapper, PersonService personService) {
        this.modelMapper = modelMapper;
        this.personService = personService;
    }

    @GetMapping("/block")
    public List<PersonDTO> getBlockPerson() {
        return personService.findByActiveFalse().stream().map(
                this::convertToPersonDTO).collect(Collectors.toList());
    }

    @GetMapping("/block/{id}")
    public String block(@PathVariable Long id) {
        Person person = personService.findById(id);
        person.setActive(false);
        personService.save(person);
        log.warn("Пользователь с id " + id + " заблокирован!");
        return "Пользователь " + person.getName() + " заблокирован!";
    }

    @GetMapping("/unlock")
    public List<PersonDTO> getUnlockPerson() {
        return personService.findByActiveTrue().stream().map(
                this::convertToPersonDTO).collect(Collectors.toList());
    }

    @GetMapping("/unlock/{id}")
    public String unlock(@PathVariable Long id) {
        Person person = personService.findById(id);
        person.setActive(true);
        personService.save(person);
        log.warn("Пользователь с id " + id + " разблокирован!");
        return "Пользователь " + person.getName() + " разблокирован!";
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

}
