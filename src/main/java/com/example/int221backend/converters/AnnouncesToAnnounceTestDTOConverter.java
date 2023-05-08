package com.example.int221backend.converters;

import com.example.int221backend.dtos.AnnounceTestDTO;
import com.example.int221backend.dtos.CategoryDTO;
import com.example.int221backend.entities.Announces;
import org.springframework.stereotype.Component;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
@Component
public class AnnouncesToAnnounceTestDTOConverter implements Converter<Announces, AnnounceTestDTO> {
    @Override
    public AnnounceTestDTO convert(MappingContext<Announces, AnnounceTestDTO> context) {
        Announces source = context.getSource();
        AnnounceTestDTO destination = context.getDestination();
        if (destination == null) {
            destination = new AnnounceTestDTO();
        }
        destination.setId(source.getAnnouncementId());
        destination.setAnnouncementTitle(source.getAnnouncementTitle());
        destination.setAnnouncementDescription(source.getAnnouncementDescription());
        destination.setPublishDate(source.getPublishDate());
        destination.setCloseDate(source.getCloseDate());
        destination.setAnnouncementDisplay(source.getAnnouncementDisplay().name());
        destination.setCategoriesObject(new CategoryDTO(source.getCategoriesObject().getCategoryName()));
        return destination;
    }
}
