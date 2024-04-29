package com.example.event5.demo_event5.service;

import com.example.event5.demo_event5.model.Content;
import com.example.event5.demo_event5.model.Event;
import com.example.event5.demo_event5.model.FileTopic;
import com.example.event5.demo_event5.model.Url;
import com.example.event5.demo_event5.repository.ContentRepository;
import com.example.event5.demo_event5.repository.EventRepository;
import com.example.event5.demo_event5.repository.FileTopicRepository;
import com.example.event5.demo_event5.repository.UrlRepository;
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
public class AdminService {
    private final EventRepository eventRepository;
    private final ContentRepository contentRepository;
    private final FileTopicRepository fileTopicRepository;
    private final UrlRepository urlRepository;
    public void processUpload(String adminName,String yearOption,String topic,String[] contents,String contents1, MultipartFile[] files,String[] urls) throws IOException {
        //creating new event object
        Event event=new Event();
        event.setAdminName(adminName);
        event.setYearOption(yearOption);
        event.setTopic(topic);
        //for saving contents
        List<Content> m1=new ArrayList<>();
        //for saving fileTopics
        List<FileTopic> m2=new ArrayList<>();
        //for saving Url
        List<Url> m3=new ArrayList<>();

        //for storing contents
        if(contents1!=null) {
            if(contents1.equals("null")){
                contents1=null;
                System.out.println(contents1);
            }else {
                Content c=new Content();
                c.setContent(contents1);
                c.setEvent(event);
                System.out.println(contents1);
                m1.add(c);
            }
        }
        else {
            System.out.println("2");
            if(contents!=null && contents.length>0) {
                for (String content : contents) {
                    Content c=new Content();
                    c.setContent(content);
                    c.setEvent(event);
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
                   FileTopic fileTopic = new FileTopic();
                   fileTopic.setName(file.getOriginalFilename());
                   fileTopic.setSize(file.getSize());
                   fileTopic.setContentType(file.getContentType());
                   fileTopic.setData(file.getBytes());
                   fileTopic.setEvent(event);
                   m2.add(fileTopic);
               }
            }
        }

        //for storing urls
        if(urls!=null && urls.length>0) {
            for(String url:urls){
               if(!url.isEmpty() && url!=null){
                   Url u=new Url();
                   u.setName(url);
                   u.setEvent(event);
                   m3.add(u);
               }
            }
        }
        //save in event object
        event.setContents(m1);
        event.setFileList(m2);
        event.setUrls(m3);
        event.setStatus(true);
        eventRepository.save(event);

    }

    public void processUpload1(Long id,String adminName, String yearOption, String topic, String[] contents, String contents1, MultipartFile[] files, String[] urls) throws IOException {
      Event event=eventRepository.findById(id).get();
        System.out.println("eventId:"+event.getId());
        event.setAdminName(adminName);
        event.setYearOption(yearOption);
        event.setTopic(topic);
        //for saving contents
        List<Content> m1=new ArrayList<>();
        //for saving fileTopics
        List<FileTopic> m2=new ArrayList<>();
        //for saving Url
        List<Url> m3=new ArrayList<>();
        System.out.println("Process upload 1");
        if(contents1!=null){
            if(contents1.equals("null")){
                contents1=null;
                System.out.println(contents1);
            }else {
                Content c=new Content();
                c.setContent(contents1);
                c.setEvent(event);
                System.out.println(contents1);
                m1.add(c);

            }

            System.out.println("1");
        }else {
            System.out.println("2");
            if (contents != null && contents.length > 0) {
                for (String content : contents) {
                    Content c = new Content();
                    c.setContent(content);
                    c.setEvent(event);
                    System.out.println("_________________________");
                    System.out.println("Content from CKEditor: " + content);
                    m1.add(c);

                }

            }
        }
            System.out.println("---------1");

            //for storing files
            if (files != null && files.length > 0) {
                for(MultipartFile file:files){
                    if(!file.isEmpty() && file!=null){
                        FileTopic fileTopic = new FileTopic();
                        fileTopic.setName(file.getOriginalFilename());
                        fileTopic.setSize(file.getSize());
                        fileTopic.setContentType(file.getContentType());
                        fileTopic.setData(file.getBytes());
                        fileTopic.setEvent(event);
                        m2.add(fileTopic);
                    }
                }
            }

            //for storing urls
            if(urls!=null && urls.length>0) {
                for(String url:urls){
                    if(!url.isEmpty() && url!=null){
                        Url u=new Url();
                        u.setName(url);
                        u.setEvent(event);
                        m3.add(u);
                    }
                }
            }
            System.out.println(event.getId());
            System.out.println(event.getContents().size());
            System.out.println(event.getUrls().size());
            List<Content> c=event.getContents();
            List<FileTopic> f=event.getFileList();
            List<Url> u=event.getUrls();
            c.addAll(m1);
            f.addAll(m2);
            u.addAll(m3);
            event.setContents(c);
            event.setFileList(f);
            event.setUrls(u);
            eventRepository.save(event);
       }


    public Page<Event> getEvents(Pageable pageable) {
          return eventRepository.findAll(pageable);
    }
}


