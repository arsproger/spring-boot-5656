package com.arsen.services;

import com.arsen.exceptions.PersonNotFoundException;
import com.arsen.models.Person;
import com.arsen.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    private final UserRepository repository;

    @Autowired
    public PersonService(UserRepository repository) {
        this.repository = repository;
    }

    public List<Person> findAll() {
        return repository.findAll();
    }

    public Person findById(Long id) {
        return repository.findById(id).orElseThrow(PersonNotFoundException::new);
    }

    public Long save(Person person) {
        return repository.save(person).getId();
    }

}
