package com.example.int221backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnounceDTO {
    private Integer id;
    private String announcementTitle;
    private String publishDate;
    private String closeDate;
    private String announcementDisplay;
    public Integer viewCount;
    @JsonIgnore
    private CategoryDTO categoriesObject;

    public String getAnnouncementCategory() {
        return categoriesObject.getCategoryName();
    }
}
