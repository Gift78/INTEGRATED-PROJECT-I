package com.example.int221backend.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "announces")
public class Announces {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int announcementId;
    private String announcementTitle;
    private String announcementDescription;
    @Column(nullable = true)
    private String publishDate;
    @Column(nullable = true)
    private String closeDate;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Categories categoriesObject;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Y','n') DEFAULT 'N'")
    private AnnouncementDisplay announcementDisplay = AnnouncementDisplay.N;
}
