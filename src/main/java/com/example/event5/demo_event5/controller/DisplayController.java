package com.example.event5.demo_event5.controller;

import com.example.event5.demo_event5.model.Contact;
import com.example.event5.demo_event5.model.Event;
import com.example.event5.demo_event5.repository.ContactRepository;
import com.example.event5.demo_event5.repository.EventRepository;
import com.example.event5.demo_event5.service.AdminService;
import com.example.event5.demo_event5.service.ContactService;
import com.example.event5.demo_event5.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequiredArgsConstructor
public class DisplayController {
    private final EventService eventService;
    private final AdminService adminService;
    private final ContactService contactService;
    private final EventRepository eventRepository;
    private final ContactRepository contactRepository;
 @GetMapping("/search/{yearOption}")
    public String search(@RequestParam(defaultValue = "",required = false,name = "searchQuery") String query, Model model, @RequestParam(defaultValue = "0") int page,
                         @PathVariable String yearOption){
     int pageSize = 12;
     Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());

  if (query != null && !query.isEmpty()) {
      System.out.println(query);
      Page<Event> eventPage=eventService.getEventsWithPagination(query,yearOption,pageable);
      for (Event event:eventPage){
          System.out.println(event.getTopic());
      }
      model.addAttribute("yearOption",yearOption);
      model.addAttribute("events",eventPage.getContent());
      model.addAttribute("currentPage",page);
      model.addAttribute("totalPages",eventPage.getTotalPages());
  }else{
      System.out.println("Hii");
      Page<Event> eventPage1 = eventRepository.findByYearOption(yearOption,pageable);
      model.addAttribute("yearOption",yearOption);
      model.addAttribute("events",eventPage1.getContent());
      model.addAttribute("currentPage",page);
      model.addAttribute("totalPages",eventPage1.getTotalPages());
  }
  return "display";
 }
    @GetMapping("/search")
    public String search4(@RequestParam(defaultValue = "",required = false,name = "searchQuery") String query, Model model, @RequestParam(defaultValue = "0") int page){
        int pageSize = 12;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());

        if (query != null && !query.isEmpty()) {
            System.out.println(query);
            Page<Event> eventPage=eventRepository.findByQuery(query,pageable);
            for (Event event:eventPage){
                System.out.println(event.getTopic());
            }
            model.addAttribute("events",eventPage.getContent());
            model.addAttribute("currentPage",page);
            model.addAttribute("totalPages",eventPage.getTotalPages());
        }else{
            System.out.println("Hii");
            Page<Event> eventPage1 = eventRepository.findAll(pageable);
            model.addAttribute("events",eventPage1.getContent());
            model.addAttribute("currentPage",page);
            model.addAttribute("totalPages",eventPage1.getTotalPages());
        }
        return "display";
    }

    @GetMapping("/searchContacts")
    public String search1(@RequestParam(defaultValue = "",required = false,name = "searchQuery") String query,Model model, @RequestParam(defaultValue = "0") int page){
        int pageSize = 12;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());

        if (query != null && !query.isEmpty()) {
            Page<Contact> eventPage = contactService.getContactsWithPagination(query,pageable);
            model.addAttribute("contacts",eventPage.getContent());
            model.addAttribute("currentPage",page);
            model.addAttribute("totalPages",eventPage.getTotalPages());
        }else{
            Page<Contact> eventPage1=contactRepository.findByStatus("Open",pageable);
            model.addAttribute("contacts",eventPage1.getContent());
            model.addAttribute("currentPage",page);
            model.addAttribute("totalPages",eventPage1.getTotalPages());
        }

        return "contactdisplay";
    }
    @GetMapping("/searchContacts1")
    public String search2(@RequestParam(defaultValue = "",required = false,name = "searchQuery") String query,Model model, @RequestParam(defaultValue = "0") int page){
        int pageSize = 12;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());

        if (query != null && !query.isEmpty()) {
            Page<Contact> eventPage = contactService.getContactsWithPagination1(query,pageable);
            model.addAttribute("contacts",eventPage.getContent());
            model.addAttribute("currentPage",page);
            model.addAttribute("totalPages",eventPage.getTotalPages());
        }else{
            Page<Contact> eventPage1=contactRepository.findByStatus("Close",pageable);
            model.addAttribute("contacts",eventPage1.getContent());
            model.addAttribute("currentPage",page);
            model.addAttribute("totalPages",eventPage1.getTotalPages());
        }
        return "contactdisplay1";
    }


}
