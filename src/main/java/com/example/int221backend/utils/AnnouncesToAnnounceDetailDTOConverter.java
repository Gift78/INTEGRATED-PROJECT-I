package com.example.int221backend.utils;

import com.example.int221backend.dtos.AnnounceDetailDTO;
import com.example.int221backend.dtos.CategoryDTO;
import com.example.int221backend.entities.Announces;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class AnnouncesToAnnounceDetailDTOConverter implements Converter<Announces, AnnounceDetailDTO> {

    @Override
    public AnnounceDetailDTO convert(MappingContext<Announces, AnnounceDetailDTO> context) {
        Announces source = context.getSource();
        AnnounceDetailDTO destination = context.getDestination();

        if (destination == null) {
            destination = new AnnounceDetailDTO();
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
