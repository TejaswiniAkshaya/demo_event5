package com.example.event5.demo_event5.repository;

import com.example.event5.demo_event5.model.Contact;
import com.example.event5.demo_event5.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long>{
    @Query("SELECT e FROM Contact e WHERE e.status = :status")
    Page<Contact> findByStatus(String status, Pageable pageable);



    Page<Contact> findByStudentNameContainingOrUsernameContainingOrYearContainingOrSubBranchOrSectionContainingOrMobileNumContainingOrEmailContainingAndStatusContainingOrContactContentListContentContaining(String query, String query1, String query2, String query3, String query4, String query5, String query6,  String open,String query7, Pageable pageable);

    @Query("SELECT c FROM Contact c " +
            "WHERE (c.studentName LIKE %:query% OR " +
            "c.Year LIKE %:query% OR " +
            "c.subBranch LIKE %:query% OR " +
            "c.section LIKE %:query% OR " +
            "c.mobileNum LIKE %:query% OR " +
            "EXISTS (SELECT cc FROM ContactContent cc WHERE cc.contact = c AND cc.content LIKE %:query%) OR " +
            "c.username LIKE %:query% OR " +
            "c.email LIKE %:query%) AND " +
            "c.status = :status")
    Page<Contact> findByQueryAndStatus(
            String query,
            String status,
            Pageable pageable);
}

