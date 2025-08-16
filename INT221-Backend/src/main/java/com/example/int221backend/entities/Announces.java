package com.example.int221backend.entities;

import com.example.int221backend.validators.ValidDisplay;
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
@ValidDisplay
public class Announces {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int announcementId;
    @NotBlank @Size(min = 1, max = 200)
    private String announcementTitle;
    @NotBlank @Size(min = 1, max = 10000)
    private String announcementDescription;
    private String publishDate;
    private String closeDate;
    private int viewCount;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Categories categoriesObject;

    @Column(columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String announcementDisplay = "N";

    @Column(insertable=false, updatable=false)
    private int categoryId;
}
