package com.example.int221backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnounceDetailDTO {
    private Integer id;
    private String announcementTitle;
    private String announcementDescription;
    private String publishDate;
    private String closeDate;
    private String announcementDisplay;
    @JsonIgnore
    private CategoryDTO categoriesObject;
    public String getAnnouncementCategory(){
        return categoriesObject.getCategoryName();

    }
    public int viewCount;
}
