package com.example.event5.demo_event5.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String emailTo;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @OneToMany(mappedBy = "email",cascade = CascadeType.ALL)
    private List<EmailFile> emailFileList;
}
