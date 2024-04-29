package com.example.event5.demo_event5.controller;

import com.example.event5.demo_event5.model.*;
import com.example.event5.demo_event5.repository.*;
import com.example.event5.demo_event5.service.AdminService;
import com.example.event5.demo_event5.service.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.bouncycastle.math.raw.Mod;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final EventRepository eventRepository;
    private final ContentRepository contentRepository;
    private final FileTopicRepository fileTopicRepository;
    private final UrlRepository urlRepository;
    private final PersonRepository personRepository;
    private final EmailService emailService;
    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("event",new Event());
        return "index";
    }
    @GetMapping("/display/AdminView")
    public String AdminView(Model model){
        return "dashboard1";
    }
    @PostMapping("/upload")
    public String upload(@RequestParam(value = "adminName") String adminName,
            @RequestParam(value = "yearOption") String yearOption,
            @RequestParam(value = "topic",required= false) String topic, @RequestParam(value = "contents[]",required = false) String[] contents,
                         @RequestParam(value = "contents",required = false) String contents1
                         , @RequestParam(value = "files",required = false) MultipartFile[] files,
                         @RequestParam(value = "links", required = false) String[] urls) throws IOException {
        System.out.println("HII");
        adminService.processUpload(adminName,yearOption,topic,contents,contents1,files,urls);
        List<Person> persons=new ArrayList<>();
        if(yearOption.equals("1stYear")){
          persons.addAll(personRepository.findByYear(yearOption));
        }else if(yearOption.equals("2ndYear")){
          persons.addAll(personRepository.findByYear(yearOption));
        }else if(yearOption.equals("3rdYear")){
            persons.addAll(personRepository.findByYear(yearOption));
        }else if(yearOption.equals("4thYear")){
         persons=personRepository.findByYear(yearOption);
        }else if(yearOption.equals("Faculty")){
            List<Person> people=personRepository.findByYear(yearOption);
            List<Person> people1=personRepository.findByYear("Admin");
            persons.addAll(people1);
            persons.addAll(people);
        }else if(yearOption.equals("SportsClubs") || yearOption.equals("DanceClubs") || yearOption.equals("MusicClubs") || yearOption.equals("TechnicalClubs") || yearOption.equals("Permissions")
        || yearOption.equals("Events")){
            List<Person> people=personRepository.findByYear("1stYear");
            List<Person> people1=personRepository.findByYear("2ndYear");
            List<Person> people2=personRepository.findByYear("3rdYear");
            List<Person> people3=personRepository.findByYear("4thYear");
            persons.addAll(people1);
            persons.addAll(people2);
            persons.addAll(people3);
            persons.addAll(people);

        }else if(yearOption.equals("Admin")){
            List<Person> people=personRepository.findByYear("Admin");
            persons.addAll(people);
        }else if(yearOption.equals("Placements")) {
            List<Person> people1 = personRepository.findByYear("3rdYear");
            List<Person> people=personRepository.findByYear("4thYears");
            persons.addAll(people);
            persons.addAll(people1);
        }else{
            persons.addAll(personRepository.findAll());
        }
        for (Person person:persons){
         String to=person.getUserEmail();
         String subject="an event added by: "+adminName;
         String text = "A notification from Domain "+yearOption+" Block regarding Topic "+topic;
         emailService.sendEmail(to,subject,text);
        }
        return "redirect:/index";

    }


    @GetMapping("/editEvent/{id}")
    public String editEvent(@PathVariable Long id,Model model){
        Event event=eventRepository.findById(id).orElse(new Event());
        if(event.getContents()==null){
            event.setContents(new ArrayList<>());
        }
        model.addAttribute("event",event);
        return "edit";
    }

    @PutMapping("edit/{id}/updateContent/{contentId}")
    public ResponseEntity<String> updateContent(@PathVariable Long id, @PathVariable Long contentId, @RequestBody String updatedContent) {
        System.out.println(updatedContent);
        Content c=contentRepository.getReferenceById(contentId);
        c.setContent(updatedContent);
        contentRepository.save(c);
        return new ResponseEntity<>("Content updated successfully", HttpStatus.OK);

    }
    @DeleteMapping("edit/{id}/deleteContent/{contentId}")
    public ResponseEntity<String> deleteContent(@PathVariable Long id,@PathVariable Long contentId){
        Event m=eventRepository.findById(id).get();
        List<Content> l=m.getContents();
        for(int i=0;i<l.size();i++){
            Content c=l.get(i);
            if(c.getId()==contentId){
                l.remove(i);
                break;
            }
        }
        m.setContents(l);
        contentRepository.deleteById(contentId);
        System.out.println("HII");
        return new ResponseEntity<>("Content updated successfully", HttpStatus.OK);
    }

    @GetMapping("event/{eventId}/delete")
    public String deleteEvent(@PathVariable Long eventId){
        eventRepository.deleteById(eventId);
        return "redirect:/events";
    }
    @PostMapping("/edit/{id}/editEvent")
    public String editEvent1(@PathVariable Long id,
                             @RequestParam(value = "adminName") String adminName,
                             @RequestParam(value = "yearOption") String yearOption,
                             @RequestParam(value = "topic",required= false) String topic, @RequestParam(value = "contents[]",required = false) String[] contents,
                             @RequestParam(value = "contents",required = false) String contents1
                            ,@RequestParam(value = "files",required = false) MultipartFile[] files,
                             @RequestParam(value = "links", required = false) String[] urls) throws IOException {
        System.out.println("Hello");
        adminService.processUpload1(id,adminName,yearOption,topic,contents,contents1,files,urls);
        return "display";

    }
    @DeleteMapping("edit/{id}/deleteFile/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id,@PathVariable Long fileId){
        Event m=eventRepository.findById(id).get();
        List<FileTopic> l=m.getFileList();
        for(int i=0;i<l.size();i++){
            FileTopic c=l.get(i);
            if(c.getId()==fileId){
                l.remove(i);
                break;
            }
        }
        m.setFileList(l);

        fileTopicRepository.deleteById(fileId);
        return new ResponseEntity<>("File Deleted Successfully", HttpStatus.OK);
    }

    @DeleteMapping("edit/{id}/deleteUrl/{urlId}")
    public ResponseEntity<String> deleteUrl(@PathVariable Long id,@PathVariable Long urlId){
        Event m=eventRepository.findById(id).get();
        List<Url> l=m.getUrls();
        for(int i=0;i<l.size();i++){
            Url u=l.get(i);
            if(u.getId()==urlId){
                l.remove(i);
                break;
            }
        }
        m.setUrls(l);
        urlRepository.deleteById(urlId);
        return new ResponseEntity<>("Url Deleted successfully", HttpStatus.OK);
    }


    /*----------*/
    @GetMapping("/downloadfile")
    public void downloadFile(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        Optional<FileTopic> fileTopic = fileTopicRepository.findById(id);
        if (fileTopic != null) {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=" + fileTopic.get().getName();
            response.setHeader(headerKey, headerValue);
            response.getOutputStream().write(fileTopic.get().getData());
            response.getOutputStream().close();
        }
    }
    @GetMapping("/image")
    public void showImage(@Param("id") Long id, HttpServletResponse response, Optional<FileTopic> fileTopic) throws IOException {
        fileTopic = fileTopicRepository.findById(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif, image/pdf,image/xlsx,image/docx,image/txt");
        response.getOutputStream().write(fileTopic.get().getData());
        response.getOutputStream().close();
    }
    private String determineContentType(String fileName) {
        Objects.requireNonNull(fileName);
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }

        switch (extension.toLowerCase()) {
            case "txt":
                return "text/plain";
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "csv":
                return "text/csv";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "mp3":
                return "audio/mpeg";
            case "wav":
                return "audio/wav";
            case "flac":
                return "audio/flac";
            case "mp4":
                return "video/mp4";
            case "avi":
                return "video/x-msvideo";
            case "mov":
                return "video/quicktime";
            case "zip":
                return "application/zip";
            case "rar":
                return "application/x-rar-compressed";
            case "7z":
                return "application/x-7z-compressed";
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "py":
                return "text/x-python";
            case "java":
                return "text/x-java-source";
            default:
                return "application/octet-stream"; // default to binary data
        }
    }

    @GetMapping("/viewfile")
    public ResponseEntity<InputStreamResource> viewFile(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        Optional<FileTopic> fileTopicOptional = fileTopicRepository.findById(id);

        if (fileTopicOptional.isPresent()) {
            FileTopic fileTopic = fileTopicOptional.get();
            byte[] fileData = fileTopic.getData();
            String contentType = fileTopic.getContentType();

            // Convert docx files to PDF format
            if (contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                fileData = convertDocxToPdf(fileData);
                contentType = "application/pdf"; // Update content type to PDF
            }

            // Set the appropriate content type for the response
            response.setContentType(contentType);

            // Set content disposition to inline so the file opens in the browser
            response.setHeader("Content-Disposition", "inline; filename=" + fileTopic.getName());

            // Write the file data to the response output stream
            String absoluteUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/viewfile?id=" + id)
                    .toUriString();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileTopic.getName())
                    .body(new InputStreamResource(new ByteArrayInputStream(fileData)));

        } else {
            // Handle file not found error
            return ResponseEntity.notFound().build();
        }


    }

    // Method to convert docx files to PDF format
    private byte[] convertDocxToPdf(byte[] docxData) throws IOException {
        try (XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(docxData));
             ByteArrayOutputStream pdfOutput = new ByteArrayOutputStream()) {

            // Create a new PDF document
            document.write(pdfOutput);
            return pdfOutput.toByteArray();
        }
    }














}
