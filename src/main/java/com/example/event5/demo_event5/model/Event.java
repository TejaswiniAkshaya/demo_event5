package com.example.event5.demo_event5.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Event extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String topic;
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private List<FileTopic> fileList;
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private List<Url> urls;
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private List<Content> contents;
    private String adminName;
    private String yearOption;
    private Boolean status;

}
