package com.example.event5.demo_event5.service;

import com.example.event5.demo_event5.model.*;
import com.example.event5.demo_event5.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public void processUpload(String studentName, String year, String subBranch, String section, String mobileNum, String[] contents, String contents1, MultipartFile[] files,String userName,String userEmail) throws IOException {
        Contact contact=new Contact();
        contact.setStudentName(studentName);
        contact.setYear(year);
        contact.setMobileNum(mobileNum);
        contact.setSubBranch(subBranch);
        contact.setSection(section);
        contact.setStatus("Open");
        contact.setUsername(userName);
        contact.setEmail(userEmail);
        //for saving contents
        List<ContactContent> m1=new ArrayList<>();
        //for saving fileContact
        List<FileContact> m2=new ArrayList<>();

        if(contents1!=null) {
            if(contents1.equals("null")){
                contents1=null;
                System.out.println(contents1);
            }else {
                ContactContent c=new ContactContent();
                c.setContent(contents1);
                c.setContact(contact);
                System.out.println(contents1);
                m1.add(c);
            }
        }else {
            System.out.println("2");
            if(contents!=null && contents.length>0) {
                for (String content : contents) {
                    ContactContent c=new ContactContent();
                    c.setContent(content);
                    c.setContact(contact);
                    System.out.println("_________________________");
                    System.out.println("Content from CKEditor: " + content);
                    m1.add(c);

                }

            }
        }

        //for storing files
        if (files != null && files.length > 0) {
            for(MultipartFile file:files){
                if(!file.isEmpty() && file!=null){
                    FileContact fileTopic = new FileContact();
                    fileTopic.setName(file.getOriginalFilename());
                    fileTopic.setSize(file.getSize());
                    fileTopic.setContentType(file.getContentType());
                    fileTopic.setData(file.getBytes());
                    fileTopic.setContact(contact);
                    m2.add(fileTopic);
                }
            }
        }
        contact.setContactContentList(m1);
        contact.setFileContacts(m2);
        contactRepository.save(contact);
    }

    public Page<Contact> getEvents(Pageable pageable) {
        return contactRepository.findByStatus("Open",pageable);
    }

    public Page<Contact> getEvents1(Pageable pageable) {

        return contactRepository.findByStatus("Close",pageable);
    }

    public Page<Contact> getContactsWithPagination(String query, Pageable pageable) {
        return contactRepository.findByQueryAndStatus(query ,"Open",  pageable);

    }

    public Page<Contact> getContactsWithPagination1(String query, Pageable pageable) {
        return contactRepository.findByQueryAndStatus(query ,"Close",  pageable);
    }
}
