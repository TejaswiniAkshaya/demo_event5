package com.example.event5.demo_event5.service;

import com.example.event5.demo_event5.model.Event;
import com.example.event5.demo_event5.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public Page<Event> getEventsWithPagination(String query, String yearOption, Pageable pageable) {
        return eventRepository.findByQueryAndYearOption(query, yearOption, pageable);
    }
}
