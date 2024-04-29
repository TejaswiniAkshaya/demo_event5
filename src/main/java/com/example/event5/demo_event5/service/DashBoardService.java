package com.example.event5.demo_event5.service;

import com.example.event5.demo_event5.model.Event;
import com.example.event5.demo_event5.repository.ContentRepository;
import com.example.event5.demo_event5.repository.EventRepository;
import com.example.event5.demo_event5.repository.FileTopicRepository;
import com.example.event5.demo_event5.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.awt.print.Pageable;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    private final EventRepository eventRepository;
    private final ContentRepository contentRepository;
    private final UrlRepository urlRepository;
    private final FileTopicRepository fileTopicRepository;
}
