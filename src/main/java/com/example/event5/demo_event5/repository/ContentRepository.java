package com.example.event5.demo_event5.repository;

import com.example.event5.demo_event5.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content,Long> {

}
