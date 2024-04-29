package com.example.event5.demo_event5.model;

import jakarta.persistence.*;
import lombok.*;

@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Url extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "event_id",nullable = false)
    private Event event;
    public Url(String name){
        this.name=name;
    }
}
