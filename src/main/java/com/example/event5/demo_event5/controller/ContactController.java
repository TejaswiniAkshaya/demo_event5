package com.example.event5.demo_event5.controller;

import com.example.event5.demo_event5.model.*;
import com.example.event5.demo_event5.repository.ContactRepository;
import com.example.event5.demo_event5.repository.FileContactRepository;
import com.example.event5.demo_event5.repository.PersonRepository;
import com.example.event5.demo_event5.service.ContactService;
import com.example.event5.demo_event5.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.bouncycastle.math.raw.Mod;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    private final ContactRepository contactRepository;
    private final PersonRepository personRepository;
    private final EmailService emailService;
    private final FileContactRepository fileContactRepository;
    @GetMapping("/contactMessage")
    public String contactMessage(Model model){
        model.addAttribute("contact",new Contact());
        return "contact";
    }
    @PostMapping("/contact")
    public String contactUpload(@RequestParam(value = "studentName") String studentName,
                                @RequestParam(value = "Year") String Year,
                                @RequestParam(value = "subBranch",required= false) String subBranch,
                                @RequestParam(value = "section",required = false) String section,
                                @RequestParam(value = "mobileNum",required = false) String mobileNum
            , @RequestParam(value = "contents[]",required = false) String[] contents,
                                @RequestParam(value = "contents",required = false) String contents1
            , @RequestParam(value = "files",required = false) MultipartFile[] files,
            Authentication authentication
                             ) throws IOException {

            String userName = authentication.getName();
            Person person = personRepository.findByUserName(userName);
            String email = person.getUserEmail();

            contactService.processUpload(studentName, Year, subBranch, section, mobileNum, contents, contents1, files, userName, email);

        String subject="Message To Hod in regarding Student "+userName+" Email:"+email;
        String message="Student Query "+email;
        emailService.sendEmail("sathiakshaya123@gmail.com",subject,message);
        return "redirect:/dashboard";
    }
    @GetMapping("/contacts")
    public String displayContacts(Model model,@RequestParam(defaultValue = "0") int page){
        int pageSize = 12;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        Page<Contact> eventPage = contactService.getEvents(pageable);
        model.addAttribute("contacts",eventPage.getContent());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",eventPage.getTotalPages());
        return "contactdisplay";
    }
    @GetMapping("/contacts1")
    public String displayContacts1(Model model,@RequestParam(defaultValue = "0") int page){
        int pageSize = 12;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        Page<Contact> eventPage = contactService.getEvents1(pageable);
        model.addAttribute("contacts",eventPage.getContent());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",eventPage.getTotalPages());
        return "contactdisplay1";
    }


    @GetMapping("/contact1/{id}/view")
    public String viewEvent(Model model, @PathVariable Long id){
       Contact contact=contactRepository.findById(id).get();
       model.addAttribute("contact",contact);
        model.addAttribute("username",contact.getUsername());
        model.addAttribute("email",contact.getEmail());
        return "view2";
    }
    @GetMapping("/contact1/{id}/email")
    public String displayEmail(Model model,@PathVariable Long id){
        Contact contact=contactRepository.findById(id).get();
        Email email=new Email();
        model.addAttribute("email",email);
        model.addAttribute("contact",contact);
        return "email";
    }
    @GetMapping("/contact1/{id}/delete")
    public String delete(Model model,@PathVariable Long id){
        contactRepository.deleteById(id);
        return "redirect:/dashboard";
    }
    @GetMapping("/contact1/{id}/close")
    public String close(Model model,@PathVariable Long id){
        Contact contact=contactRepository.findById(id).get();
        contact.setStatus("Close");
        contactRepository.save(contact);
        return "redirect:/dashboard";
    }
    @GetMapping("/contact1/{id}/approve")
    public String approve(Model model,@PathVariable Long id){
        Contact contact=contactRepository.findById(id).get();
        String subject="Message from Hod in regarding to your query";
        String message="Your Query request is Approved";
        emailService.sendEmail(contact.getEmail(),subject,message);
        contact.setStatus("Close");
        contactRepository.save(contact);
        return "redirect:/dashboard";
    }
    @GetMapping("/contact1/{id}/disapprove")
    public String disapprove(Model model,@PathVariable Long id){
        Contact contact=contactRepository.findById(id).get();
        String subject="Message from Hod in regarding to your query";
        String message="Your Query request is Disapproved";
        emailService.sendEmail(contact.getEmail(),subject,message);
        contact.setStatus("Close");
        contactRepository.save(contact);
        return "redirect:/dashboard";
    }

    @PostMapping("/submitEmail/{id}")
    public String submitEmail(@RequestParam(value = "contents",required = false) String contents
            , @RequestParam(value = "files",required = false) MultipartFile[] files,@PathVariable Long id) throws IOException, MessagingException {
        System.out.println("Email HII");

        Contact contact=contactRepository.findById(id).get();
        String email=contact.getEmail();
        String subject="This Mail is from Hod,in reaction to your Query";
        String text=contents;
        System.out.println("CKEditor content received: " + contents);
         System.out.println(contact.getEmail());
         System.out.println(files.length);
        if (files.length>1 && (files[0]!=null || !files[0].isEmpty())) {
            emailService.sendEmail1(email,subject,text,files);
        }else{
           emailService.sendEmail(email,subject,text);
        }
        contact.setStatus("Close");
        contactRepository.save(contact);
        // Return a response indicating success or any other necessary information
        return "redirect:/dashboard";
    }




    @GetMapping("/viewfiles")
    public ResponseEntity<InputStreamResource> viewFile(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        Optional<FileContact> fileTopicOptional = fileContactRepository.findById(id);

        if (fileTopicOptional.isPresent()) {
            FileContact fileTopic = fileTopicOptional.get();
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
                    .path("/viewfiles?id=" + id)
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

    private byte[] convertDocxToPdf(byte[] docxData) throws IOException {
        try (XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(docxData));
             ByteArrayOutputStream pdfOutput = new ByteArrayOutputStream()) {

            // Create a new PDF document
            document.write(pdfOutput);
            return pdfOutput.toByteArray();
        }
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

    @GetMapping("/images")
    public void showImage(@Param("id") Long id, HttpServletResponse response, Optional<FileContact> fileTopic) throws IOException {
         fileTopic= fileContactRepository.findById(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif, image/pdf,image/xlsx,image/docx,image/txt");
        response.getOutputStream().write(fileTopic.get().getData());
        response.getOutputStream().close();
    }

    @GetMapping("/downloadfiles")
    public void downloadFile(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        Optional<FileContact> fileTopic = fileContactRepository.findById(id);
        if (fileTopic != null) {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=" + fileTopic.get().getName();
            response.setHeader(headerKey, headerValue);
            response.getOutputStream().write(fileTopic.get().getData());
            response.getOutputStream().close();
        }
    }



}
