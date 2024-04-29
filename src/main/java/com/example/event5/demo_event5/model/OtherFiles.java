package com.example.event5.demo_event5.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OtherFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long size;
    private String contentType;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] data;
    @ManyToOne
    @JoinColumn(name = "profile_id",nullable = false)
    private Profile profile;
    public OtherFiles(String name,long size,byte[] data){
        this.name=name;
        this.size=size;
        this.data=data;
        this.contentType=determineContentType(name);
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

}
