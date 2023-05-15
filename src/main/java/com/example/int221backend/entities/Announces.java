package com.example.int221backend.entities;

import com.example.int221backend.validators.ValidPublishAndCloseDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank @Size(min = 1, max = 200)
    private String announcementTitle;
    @NotBlank @Size(min = 1, max = 10000)
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
