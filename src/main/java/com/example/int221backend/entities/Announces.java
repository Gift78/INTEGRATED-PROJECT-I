package com.example.int221backend.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "announces")
public class Announces {
    @Id
    private int announcementId;
    private String announcementTitle;
    private String announcementDescription;
    private String publishDate;
    private String closeDate;

    private String announcementDisplay;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "categoryId" ,nullable = false)
    private Categories categoriesObject;
}
