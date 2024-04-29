package com.example.event5.demo_event5.repository;

import com.example.event5.demo_event5.model.FileContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileContactRepository extends JpaRepository<FileContact,Long> {
}
