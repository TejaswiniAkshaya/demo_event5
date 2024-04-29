package com.example.event5.demo_event5.listener;

import com.example.event5.demo_event5.model.Person;
import org.springframework.context.ApplicationEvent;

public class PersonEvent extends ApplicationEvent {
    private Person person;

    public PersonEvent(Object source, Person person) {
        super(source);
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
}
