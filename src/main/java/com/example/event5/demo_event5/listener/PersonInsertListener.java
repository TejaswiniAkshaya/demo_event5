package com.example.event5.demo_event5.listener;

import com.example.event5.demo_event5.model.Person;
import com.example.event5.demo_event5.model.Profile;
import com.example.event5.demo_event5.repository.PersonRepository;
import com.example.event5.demo_event5.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.event.TransactionalEventListener;

public class PersonInsertListener {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PersonRepository personRepository;

    @TransactionalEventListener
    public void handlePersonInsert(PersonEvent event) {
        Person person = event.getPerson();

        // Create a new Profile object
        Profile profile = new Profile();
        profile.setCount(true);
        profile.setUserName(person.getUserName());
        profile.setEmail(person.getUserEmail());
        profile.setPerson(person);

        // Save the Profile object
        profileRepository.save(profile);

        // Set the Profile object to the Person object
        person.setProfile(profile);
       

    }
}
