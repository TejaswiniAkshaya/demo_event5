package com.example.event5.demo_event5.repository;

import com.example.event5.demo_event5.model.Contact;
import com.example.event5.demo_event5.model.Person;
import com.example.event5.demo_event5.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Profile findByUserName(String name);

    @Query("SELECT e FROM Profile e WHERE e.year = :year")
    Page<Profile> findByYear(String year, Pageable pageable);

    Page<Profile> findByFirstNameContainingOrLastNameContainingOrUserNameContainingOrRollnoContainingOrBranchContainingOrCGPAContainingOrSectionContainingAndYearContaining(String query,String query1, String query2, String query3, String query4, String query5, String query6,String year, Pageable pageable);



}
