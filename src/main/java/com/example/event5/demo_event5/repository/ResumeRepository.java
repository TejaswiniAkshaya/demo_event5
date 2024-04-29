package com.example.event5.demo_event5.repository;

import com.example.event5.demo_event5.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<Resume,Long> {
}
