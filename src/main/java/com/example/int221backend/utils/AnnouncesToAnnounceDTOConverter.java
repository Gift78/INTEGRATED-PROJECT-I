package com.example.int221backend.utils;

import com.example.int221backend.dtos.AnnounceDTO;
import com.example.int221backend.dtos.CategoryDTO;
import com.example.int221backend.entities.Announces;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class AnnouncesToAnnounceDTOConverter implements Converter<Announces, AnnounceDTO> {
    @Override
    public AnnounceDTO convert(MappingContext<Announces, AnnounceDTO> context) {
        Announces source = context.getSource();
        AnnounceDTO destination = context.getDestination();
        if (destination == null) {
            destination = new AnnounceDTO();
        }
        destination.setId(source.getAnnouncementId());
        destination.setAnnouncementTitle(source.getAnnouncementTitle());
        destination.setPublishDate(source.getPublishDate());
        destination.setCloseDate(source.getCloseDate());
        destination.setAnnouncementDisplay(source.getAnnouncementDisplay().name());
        destination.setCategoriesObject(new CategoryDTO(source.getCategoriesObject().getCategoryName()));
        return destination;
    }
}