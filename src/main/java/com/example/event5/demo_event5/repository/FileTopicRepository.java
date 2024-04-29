package com.example.event5.demo_event5.repository;

import com.example.event5.demo_event5.model.FileTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileTopicRepository extends JpaRepository<FileTopic,Long> {
}
