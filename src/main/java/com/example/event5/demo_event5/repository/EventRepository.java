package com.example.event5.demo_event5.repository;

import com.example.event5.demo_event5.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    @Query("SELECT e FROM Event e WHERE e.yearOption = :yearOption")
    Page<Event> findByYearOption(String yearOption,Pageable pageable);
    /*Page<Event> findByTopicContainingOrYearOptionContainingOrAdminNameContainingOrContentsContentContainingOrUrlsNameContaining(String topic,String yearOption, String adminName, String content, String url, Pageable pageable);*/
    @Query("SELECT e FROM Event e " +
            "WHERE (e.topic LIKE %:query% OR e.adminName LIKE %:query% OR " +
            "EXISTS (SELECT c FROM Content c WHERE c.event = e AND c.content LIKE %:query%) OR " +
            "EXISTS (SELECT u FROM Url u WHERE u.event = e AND u.name LIKE %:query%)) " +
            "AND e.yearOption = :yearOption")
    Page<Event> findByQueryAndYearOption(
            @Param("query") String query,
            @Param("yearOption") String yearOption,
            Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (e.topic LIKE %:query% OR e.adminName LIKE %:query% OR " +
            "EXISTS (SELECT c FROM Content c WHERE c.event = e AND c.content LIKE %:query%) OR " +
            "EXISTS (SELECT u FROM Url u WHERE u.event = e AND u.name LIKE %:query%))")
    Page<Event> findByQuery(
            @Param("query") String query,
            Pageable pageable);

}
