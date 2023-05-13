package com.example.int221backend.entities;

import com.example.int221backend.validators.ValidPublishAndCloseDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "announces")
@ValidPublishAndCloseDate
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Categories categoriesObject;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Y','n') DEFAULT 'N'")
    private AnnouncementDisplay announcementDisplay = AnnouncementDisplay.N;

    @Column(insertable=false, updatable=false)
    private int categoryId;
}
