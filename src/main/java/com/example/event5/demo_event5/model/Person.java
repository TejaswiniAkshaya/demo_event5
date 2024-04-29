package com.example.event5.demo_event5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Getter
@Setter
public class Person extends BaseEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;
    @NotBlank(message="User Name must not be blank")
    private String userName;
    @NotBlank(message = "User Email must not be blank")
    private String userEmail;
    @NotBlank(message = "password not be blank")
    private String password;
    @NotBlank(message = "year should not be blank")
    private String year;
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Roles.class)
    @JoinColumn(name = "role_id",referencedColumnName = "roleId",nullable = false)
    private Roles roles;
    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private Profile profile;
}