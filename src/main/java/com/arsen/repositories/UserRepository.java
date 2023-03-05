package com.arsen.repositories;

import com.arsen.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Person, Long> {
    @Query(value = "SELECT * FROM PERSON WHERE ACTIVE = TRUE", nativeQuery = true)
    List<Person> findByActiveTrue();

    @Query(value = "SELECT * FROM PERSON WHERE ACTIVE = FALSE", nativeQuery = true)

    List<Person> findByActiveFalse();
}
