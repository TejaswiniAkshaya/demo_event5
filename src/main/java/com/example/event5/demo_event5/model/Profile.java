package com.example.event5.demo_event5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;
    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="First Name must be at least 3 characters long")
    private String firstName;

    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="First Name must be at least 3 characters long")
    private String lastName;
    @NotBlank(message="Email must not be blank")
    @Email(message = "Please provide a valid email address" )
    private String PersonalEmail;
    @NotBlank(message="Mobile number must not be blank")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    private String mobileNumber;
    private String year;
    private String branch;
    private String rollno;
    private String section;
    private String aadharNo;
    @NotBlank(message="Email must not be blank")
    @Email(message = "Please provide a valid email address" )
    private String collegeMail;

    @OneToOne(mappedBy = "profile",cascade = CascadeType.ALL)
    private Resume resume;
    @OneToMany(mappedBy = "profile",cascade = CascadeType.ALL)
    private List<CertificationFiles> certifications;
    @OneToMany(mappedBy = "profile",cascade = CascadeType.ALL)
    private List<OtherFiles> otherFiles;
    @OneToMany(mappedBy = "profile",cascade = CascadeType.ALL)
    private List<ProfileLinks> profileLinks;
    private String CGPA;
    private String userName;
    private String  email;
    private boolean count=true;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", referencedColumnName = "id",nullable = false)
    private Person person;
}
