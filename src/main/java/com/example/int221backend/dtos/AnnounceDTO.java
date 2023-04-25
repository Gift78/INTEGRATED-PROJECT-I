package com.example.int221backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnounceDTO {
    private Integer announcementId;
    private String announcementTitle;
    private String publishDate;
    private String closeDate;

    @JsonIgnore
    private CatagoryDTO categoriesObject;
    public String getCategory(){
        return categoriesObject.getCategoryName();
    }
}
