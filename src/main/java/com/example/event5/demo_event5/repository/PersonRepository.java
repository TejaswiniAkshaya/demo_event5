package com.example.event5.demo_event5.repository;

import com.example.event5.demo_event5.model.Event;
import com.example.event5.demo_event5.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person,Long> {
    @Query("SELECT e FROM Person e WHERE e.year = :year")
    List<Person> findByYear(String year);

    Person findByUserName(String name);
}
