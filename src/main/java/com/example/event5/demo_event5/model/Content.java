package com.example.event5.demo_event5.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Content extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @ManyToOne
    @JoinColumn(name = "event_id",nullable = false)
    private Event event;

}
