package com.example.event5.demo_event5.controller;

import com.example.event5.demo_event5.model.*;
import com.example.event5.demo_event5.repository.*;
import com.example.event5.demo_event5.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.bouncycastle.math.raw.Mod;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileRepository profileRepository;
    private final PersonRepository personRepository;
    private final ResumeRepository resumeRepository;
    private final OtherFilesRepository otherFilesRepository;
    private final CertificationFileRepository certificationFileRepository;
    private final ProfileLinkRepository profileLinkRepository;
    private final ProfileService profileService;
    @GetMapping("/profiles/{id}/view")
    public String display3(@PathVariable(name = "id",required = false) Long id,Model model,Authentication authentication){
        Profile profile=profileRepository.findById(id).get();
       model.addAttribute("profile",profile);
       return "AdProfile";
    }
    @GetMapping("/searchProfile")
    public String search1(@RequestParam(defaultValue = "",required = false,name = "searchQuery") String query,Model model, @RequestParam(defaultValue = "0") int page){
        int pageSize = 12;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("CGPA").descending());

        if (query != null && !query.isEmpty()) {
            Page<Profile> eventPage = profileService.getProfilesWithPagination(query,pageable);
            model.addAttribute("profiles",eventPage.getContent());
            model.addAttribute("currentPage",page);
            model.addAttribute("totalPages",eventPage.getTotalPages());
        }else{
            Page<Profile> eventPage1= profileService.getEvents("3rdYear",pageable);
            model.addAttribute("profiles",eventPage1.getContent());
            model.addAttribute("currentPage",page);
            model.addAttribute("totalPages",eventPage1.getTotalPages());
        }
        return "ThirdYears";
    }
    @GetMapping("/searchProfile1")
    public String search2(@RequestParam(defaultValue = "",required = false,name = "searchQuery") String query,Model model, @RequestParam(defaultValue = "0") int page){
        int pageSize = 12;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("CGPA").descending());

        if (query != null && !query.isEmpty()) {
            Page<Profile> eventPage = profileService.getProfilesWithPagination1(query,pageable);
            model.addAttribute("profiles",eventPage.getContent());
            model.addAttribute("currentPage",page);
            model.addAttribute("totalPages",eventPage.getTotalPages());
        }else{
            Page<Profile> eventPage1= profileService.getEvents("3rdYear",pageable);
            model.addAttribute("profiles",eventPage1.getContent());
            model.addAttribute("currentPage",page);
            model.addAttribute("totalPages",eventPage1.getTotalPages());
        }
        return "FourthYears";
    }
    @GetMapping("/profile1")
    public String displayProfile(Model model,Authentication authentication){
        String userName = authentication.getName();
        Person person = personRepository.findByUserName(userName);
        model.addAttribute("profile",person.getProfile());
        return "displayProfile";
    }
    @GetMapping("/profile2")
    public String displayProfile2(){
        return "displayProfile1";
    }
    @GetMapping("/3rdYears")
    public String display3rdYears(Model model,@RequestParam(defaultValue = "0") int page){
        int pageSize = 12;
        System.out.println("hii");
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("CGPA").descending());
        Page<Profile> eventPage = profileService.getEvents("3rdYear",pageable);
        model.addAttribute("profiles",eventPage.getContent());
        for(Profile p:eventPage.getContent()){
            System.out.println(p.getCollegeMail());
        }
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",eventPage.getTotalPages());
        return "ThirdYears";
    }
    @GetMapping("/4thYears")
    public String display4thYears(Model model,@RequestParam(defaultValue = "0") int page){
        int pageSize = 12;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("CGPA").descending());
        Page<Profile> eventPage = profileService.getEvents("4thYear",pageable);
        model.addAttribute("profiles",eventPage.getContent());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",eventPage.getTotalPages());
        return "FourthYears";
    }



    @GetMapping("/viewProfile")
    public String viewProfile(Authentication authentication, Model model) {

        // Get the username of the authenticated user
        String username = authentication.getName();

        // Get the authorities (roles) granted to the authenticated user
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String userName = authentication.getName();
        Person person = personRepository.findByUserName(userName);
        boolean isStudent = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"));
        if (person.getProfile() != null) {
            model.addAttribute("check", true);
        } else {
            model.addAttribute("check", false);
        }

        return "viewProfile";
    }


    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
       /* String userName = authentication.getName();
        Person person= personRepository.findByUserName(userName);
        String email = person.getUserEmail();

        if(person.getProfile()==null) {
            Profile p=new Profile();
            person.setProfile(p);
            p.setPerson(person);
           profileRepository.save(p);
            model.addAttribute("profile",person.getProfile());
            System.out.println(person.getProfile());
            System.out.println(p);
            System.out.println(p.getId());
            System.out.println(person.getProfile().getId());

        }else {
            model.addAttribute("profile", person.getProfile());
        }
        */

        return "profile";
    }


    @PostMapping("/profileUpload")
    public String profileUpload(@RequestParam(name = "firstName", required = false) String firstName,
                                @RequestParam(name = "lastName", required = false) String lastName,
                                @RequestParam(name = "PersonalEmail", required = false) String PersonalEmail,
                                @RequestParam(name = "mobileNumber", required = false) String mobileNumber,
                                @RequestParam(name = "year", required = false) String year,
                                @RequestParam(name = "branch", required = false) String branch,
                                @RequestParam(name = "rollno", required = false) String rollno,
                                @RequestParam(name = "section", required = false) String section,
                                @RequestParam(name = "aadharNo", required = false) String aadharNo,
                                @RequestParam(name = "collegeMail", required = false) String collegeMail,
                                @RequestParam(name = "CGPA", required = false) String CGPA,
                                @RequestParam(name = "resume", required = false) MultipartFile resume,
                                @RequestParam(name = "certificates", required = false) MultipartFile[] certificates,
                                @RequestParam(name = "files", required = false) MultipartFile[] files,
                                @RequestParam(name = "links", required = false) String[] links,
                                Authentication authentication) throws IOException {
        Profile p = new Profile();
        String userName = authentication.getName();
        Person person = personRepository.findByUserName(userName);
        System.out.println("id" + p.getId());
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setMobileNumber(mobileNumber);
        p.setPersonalEmail(PersonalEmail);
        p.setYear(year);
        p.setCGPA(CGPA);
        p.setAadharNo(aadharNo);
        p.setSection(section);
        p.setBranch(branch);
        p.setRollno(rollno);
        p.setCollegeMail(collegeMail);
        List<CertificationFiles> m1 = new ArrayList<>();
        List<OtherFiles> m2 = new ArrayList<>();
        List<ProfileLinks> m3 = new ArrayList<>();

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty() && file != null) {
                    OtherFiles otherFiles = new OtherFiles();
                    otherFiles.setName(file.getOriginalFilename());
                    otherFiles.setSize(file.getSize());
                    otherFiles.setContentType(file.getContentType());
                    otherFiles.setData(file.getBytes());
                    otherFiles.setProfile(p);
                    m2.add(otherFiles);
                }
            }
        }
        if (certificates != null && certificates.length > 0) {
            for (MultipartFile file : certificates) {
                if (!file.isEmpty() && file != null) {
                    CertificationFiles certificationFiles = new CertificationFiles();
                    certificationFiles.setName(file.getOriginalFilename());
                    certificationFiles.setSize(file.getSize());
                    certificationFiles.setContentType(file.getContentType());
                    certificationFiles.setData(file.getBytes());
                    certificationFiles.setProfile(p);
                    m1.add(certificationFiles);
                }
            }
        }
        if (links != null && links.length > 0) {
            for (String url : links) {
                if (!url.isEmpty() && url != null) {
                    ProfileLinks u = new ProfileLinks();
                    u.setName(url);
                    u.setProfile(p);
                    m3.add(u);
                }
            }
        }

        if (resume != null && !resume.isEmpty()) {
            Resume resume1 = new Resume();
            resume1 = new Resume();
            resume1.setName(resume.getName());
            resume1.setSize(resume.getSize());
            resume1.setContentType(resume.getContentType());
            resume1.setData(resume.getBytes());
            resume1.setProfile(p);
            p.setResume(resume1);
        }
        p.setCertifications(m1);
        p.setProfileLinks(m3);
        p.setOtherFiles(m2);
        p.setCount(false);
        String email = person.getUserEmail();
        p.setEmail(email);
        p.setUserName(userName);
        person.setProfile(p);
        p.setPerson(person);
        profileRepository.save(p);
        System.out.println("Saved Successfully");
        return "redirect:/profile";
    }

    @DeleteMapping("editProfile/{id}/deleteFile/{fileId}")

    public ResponseEntity<String> deleteFile(@PathVariable Long id, @PathVariable Long fileId) {
        Profile m = profileRepository.findById(id).get();
        List<CertificationFiles> l = m.getCertifications();
        for (int i = 0; i < l.size(); i++) {
            CertificationFiles c = l.get(i);
            if (c.getId() == fileId) {
                l.remove(i);
                break;
            }
        }
        m.setCertifications(l);
        certificationFileRepository.deleteById(fileId);
        return new ResponseEntity<>("File Deleted Successfully", HttpStatus.OK);
    }

    @DeleteMapping("editProfile1/{id}/deleteFile/{fileId}")
    public ResponseEntity<String> deleteFile1(@PathVariable Long id, @PathVariable Long fileId) {
        Profile m = profileRepository.findById(id).get();
        List<OtherFiles> l = m.getOtherFiles();
        for (int i = 0; i < l.size(); i++) {
            OtherFiles c = l.get(i);
            if (c.getId() == fileId) {
                l.remove(i);
                break;
            }
        }
        m.setOtherFiles(l);
        otherFilesRepository.deleteById(fileId);
        return new ResponseEntity<>("File Deleted Successfully", HttpStatus.OK);
    }

    @DeleteMapping("editLink/{id}/deleteUrl/{urlId}")
    public ResponseEntity<String> deleteUrl(@PathVariable Long id, @PathVariable Long urlId) {
        Profile m = profileRepository.findById(id).get();
        List<ProfileLinks> l = m.getProfileLinks();
        for (int i = 0; i < l.size(); i++) {
            ProfileLinks u = l.get(i);
            if (u.getId() == urlId) {
                l.remove(i);
                break;
            }
        }
        m.setProfileLinks(l);
        profileLinkRepository.deleteById(urlId);
        return new ResponseEntity<>("Url Deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("editProfileResume/{id}/deleteResume/{fileId}")
    public ResponseEntity<String> deleteResume(@PathVariable Long id, @PathVariable Long fileId) {
        Profile m = profileRepository.findById(id).get();
        Resume s=resumeRepository.findById(fileId).get();
        System.out.println(s.getName());
        System.out.println(fileId);
        m.setResume(null);
        resumeRepository.deleteById(fileId);
        System.out.println("Resume Deleted");
        return new ResponseEntity<>("Url Deleted successfully", HttpStatus.OK);
    }

    @PostMapping("editProfile/{id}/ProfileUpload1")
    public String profileUpload(@PathVariable Long id,
                                @RequestParam(name = "firstName", required = false) String firstName,
                                @RequestParam(name = "lastName", required = false) String lastName,
                                @RequestParam(name = "PersonalEmail", required = false) String PersonalEmail,
                                @RequestParam(name = "mobileNumber", required = false) String mobileNumber,
                                @RequestParam(name = "year", required = false) String year,
                                @RequestParam(name = "branch", required = false) String branch,
                                @RequestParam(name = "section", required = false) String section,
                                @RequestParam(name = "aadharNo", required = false) String aadharNo,
                                @RequestParam(name = "CGPA", required = false) String CGPA,
                                @RequestParam(name = "collegeMail", required = false) String collegeMail,
                                @RequestParam(name = "rollno", required = false) String rollno,
                                @RequestParam(name = "resume", required = false) MultipartFile resume,
                                @RequestParam(name = "certificates", required = false) MultipartFile[] certificates,
                                @RequestParam(name = "files", required = false) MultipartFile[] files,
                                @RequestParam(name = "links", required = false) String[] links,
                                Authentication authentication) throws IOException {
        System.out.println("Edit Page");
        Profile p = profileRepository.findById(id).get();
        String userName = authentication.getName();
        Person person = personRepository.findByUserName(userName);
        System.out.println("id" + p.getId());
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setMobileNumber(mobileNumber);
        p.setPersonalEmail(PersonalEmail);
        p.setYear(year);
        p.setCGPA(CGPA);
        p.setAadharNo(aadharNo);
        p.setSection(section);
        p.setBranch(branch);
        p.setRollno(rollno);
        p.setCollegeMail(collegeMail);
        List<CertificationFiles> m1 = new ArrayList<>();
        List<OtherFiles> m2 = new ArrayList<>();
        List<ProfileLinks> m3 = new ArrayList<>();

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty() && file != null) {
                    OtherFiles otherFiles = new OtherFiles();
                    otherFiles.setName(file.getOriginalFilename());
                    otherFiles.setSize(file.getSize());
                    otherFiles.setContentType(file.getContentType());
                    otherFiles.setData(file.getBytes());
                    otherFiles.setProfile(p);
                    m2.add(otherFiles);
                }
            }
        }
        if (certificates != null && certificates.length > 0) {
            for (MultipartFile file : certificates) {
                if (!file.isEmpty() && file != null) {
                    CertificationFiles certificationFiles = new CertificationFiles();
                    certificationFiles.setName(file.getOriginalFilename());
                    certificationFiles.setSize(file.getSize());
                    certificationFiles.setContentType(file.getContentType());
                    certificationFiles.setData(file.getBytes());
                    certificationFiles.setProfile(p);
                    m1.add(certificationFiles);
                }
            }
        }
        if(links!=null && links.length>0) {
            for(String url:links){
                if(!url.isEmpty() && url!=null){
                    ProfileLinks u=new ProfileLinks();
                    u.setName(url);
                    u.setProfile(p);
                    m3.add(u);
                }
            }
        }

        if(resume!=null && !resume.isEmpty()){

            Resume resume1=new Resume();
            resume1.setName(resume.getOriginalFilename());
            resume1.setSize(resume.getSize());
            resume1.setContentType(resume.getContentType());
            resume1.setData(resume.getBytes());
            resume1.setProfile(p);
            p.setResume(resume1);
            System.out.println(resume.getName());
        }else {
            p.setResume(null);
            System.out.println("Resume null");
        }
        p.setCertifications(m1);
        p.setProfileLinks(m3);
        p.setOtherFiles(m2);
        p.setCount(false);

        String email = person.getUserEmail();
        p.setEmail(email);
        p.setUserName(userName);
        person.setProfile(p);
        profileRepository.save(p);
        System.out.println("Saved Successfully");
        return "redirect:/dashboard";

    }
    @GetMapping("/editProfile")
    public String editProfile(Model model,Authentication authentication){
        String userName = authentication.getName();
        Person person = personRepository.findByUserName(userName);
        model.addAttribute("profile",person.getProfile());
        return "editProfile";
    }






    @GetMapping("/viewfiler")
    public ResponseEntity<InputStreamResource> viewFile(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        Optional<Resume> fileTopicOptional = resumeRepository.findById(id);

        if (fileTopicOptional.isPresent()) {
            Resume fileTopic = fileTopicOptional.get();
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
                    .path("/viewfiler?id=" + id)
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

    @GetMapping("/imager")
    public void showImage(@Param("id") Long id, HttpServletResponse response, Optional<Resume> fileTopic) throws IOException {
        fileTopic= resumeRepository.findById(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif, image/pdf,image/xlsx,image/docx,image/txt");
        response.getOutputStream().write(fileTopic.get().getData());
        response.getOutputStream().close();
    }

    @GetMapping("/downloadfiler")
    public void downloadFile(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        Optional<Resume> fileTopic = resumeRepository.findById(id);
        if (fileTopic != null) {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=" + fileTopic.get().getName();
            response.setHeader(headerKey, headerValue);
            response.getOutputStream().write(fileTopic.get().getData());
            response.getOutputStream().close();
        }
    }



    /** Certification Files **/


    @GetMapping("/viewfilec")
    public ResponseEntity<InputStreamResource> viewFilec(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        Optional<CertificationFiles> fileTopicOptional = certificationFileRepository.findById(id);

        if (fileTopicOptional.isPresent()) {
            CertificationFiles fileTopic = fileTopicOptional.get();
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
                    .path("/viewfilec?id=" + id)
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





    @GetMapping("/imagec")
    public void showImagec(@Param("id") Long id, HttpServletResponse response, Optional<CertificationFiles> fileTopic) throws IOException {
        fileTopic= certificationFileRepository.findById(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif, image/pdf,image/xlsx,image/docx,image/txt");
        response.getOutputStream().write(fileTopic.get().getData());
        response.getOutputStream().close();
    }

    @GetMapping("/downloadfilec")
    public void downloadFilec(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        Optional<CertificationFiles> fileTopic = certificationFileRepository.findById(id);
        if (fileTopic != null) {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=" + fileTopic.get().getName();
            response.setHeader(headerKey, headerValue);
            response.getOutputStream().write(fileTopic.get().getData());
            response.getOutputStream().close();
        }
    }

    /** Other Files **/



    @GetMapping("/viewfileo")
    public ResponseEntity<InputStreamResource> viewFileo(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        Optional<OtherFiles> fileTopicOptional = otherFilesRepository.findById(id);

        if (fileTopicOptional.isPresent()) {
            OtherFiles fileTopic = fileTopicOptional.get();
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
                    .path("/viewfileo?id=" + id)
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



    @GetMapping("/imageo")
    public void showImageo(@Param("id") Long id, HttpServletResponse response, Optional<OtherFiles> fileTopic) throws IOException {
        fileTopic= otherFilesRepository.findById(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif, image/pdf,image/xlsx,image/docx,image/txt");
        response.getOutputStream().write(fileTopic.get().getData());
        response.getOutputStream().close();
    }

    @GetMapping("/downloadfileo")
    public void downloadFileo(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        Optional<OtherFiles> fileTopic = otherFilesRepository.findById(id);
        if (fileTopic != null) {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=" + fileTopic.get().getName();
            response.setHeader(headerKey, headerValue);
            response.getOutputStream().write(fileTopic.get().getData());
            response.getOutputStream().close();
        }
    }

    @GetMapping("/years")
    public String handleRequest(@RequestParam(name = "year") String year) {
      if(year.equals("3rdYear")){
         return "ThirdYears";
      }else if(year.equals("4tYear")){
         return "FourthYears";
      }else{
          return "ThirdYears";
      }

    }








}
