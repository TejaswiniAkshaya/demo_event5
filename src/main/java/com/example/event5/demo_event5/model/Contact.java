package com.example.event5.demo_event5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@Entity
public class Contact extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private int contactId;
    @NotBlank(message = "Name must not be blank")
    private String studentName;
    @NotBlank(message = "Year must not be blank")
    private String Year;
    @NotBlank(message = "SubBranch must not be blank")
    private String subBranch;
    @NotBlank(message = "Section must not be blank")
    private String section;
    @NotBlank(message = "Mobile Number must not be Blank")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    private String mobileNum;
    @OneToMany(mappedBy = "contact",cascade = CascadeType.ALL)
    private List<ContactContent> contactContentList;
    @OneToMany(mappedBy = "contact",cascade = CascadeType.ALL)
    private List<FileContact> fileContacts;
    private String status;
    private String username;
    private String email;

}
