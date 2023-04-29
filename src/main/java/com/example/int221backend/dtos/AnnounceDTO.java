package com.example.int221backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class AnnounceDTO {
    private Integer id;
    private String announcementTitle;
    private String publishDate;
    private String closeDate;
    private String announcementDisplay;
    @JsonIgnore
    private CategoryDTO categoriesObject;

    public String getAnnouncementCategory() {
        return categoriesObject.getCategoryName();
    }
}
