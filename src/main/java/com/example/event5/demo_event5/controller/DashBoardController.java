package com.example.event5.demo_event5.controller;
import com.example.event5.demo_event5.model.Person;
import com.example.event5.demo_event5.repository.*;
import jakarta.servlet.http.HttpSession;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.example.event5.demo_event5.model.Event;
import com.example.event5.demo_event5.model.FileTopic;
import com.example.event5.demo_event5.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.ParseContext;

import org.apache.tika.sax.BodyContentHandler;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

@Controller
@RequiredArgsConstructor
public class DashBoardController {
    private final AdminService adminService;
    private final EventRepository eventRepository;
    private final ContentRepository contentRepository;
    private final FileTopicRepository fileTopicRepository;
    private final UrlRepository urlRepository;
    private final PersonRepository personRepository;
    @GetMapping("/events")
    public String displayEvents(Model model,@RequestParam(defaultValue = "0") int page){
        int pageSize = 12;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        Page<Event> eventPage = eventRepository.findAll(pageable);
        model.addAttribute("events",eventPage.getContent());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",eventPage.getTotalPages());
        return "display2";
    }
    @GetMapping("/event/{id}/view")
    public String viewEvent(Model model, @PathVariable Long id){
        Event event=eventRepository.findById(id).get();
        model.addAttribute("event",event);
        return "view1";
    }
    @GetMapping("/dashboard")
    public String dashBoard(Model model, Authentication authentication, HttpSession session){
        return "dashboard";
    }
    @GetMapping("/display1/{yearOption}")
    public String displayEvents(@PathVariable String yearOption, Model model,@RequestParam(defaultValue = "0") int page) {
        System.out.println(yearOption);
        int pageSize = 12;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        Page<Event> eventPage = eventRepository.findByYearOption(yearOption,pageable);
        model.addAttribute("events",eventPage);
        model.addAttribute("yearOption",yearOption);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",eventPage.getTotalPages());
       return "display";
    }





}
