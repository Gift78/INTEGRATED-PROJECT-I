package com.example.int221backend.converters;

import com.example.int221backend.dtos.AnnounceViewActiveDTO;
import com.example.int221backend.dtos.CategoryDTO;
import com.example.int221backend.entities.Announces;
import org.springframework.stereotype.Component;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
@Component
public class AnnouncesToAnnounceViewActiveDTOConverter implements Converter<Announces, AnnounceViewActiveDTO> {
    @Override
    public AnnounceViewActiveDTO convert (MappingContext<Announces, AnnounceViewActiveDTO> context) {
        Announces source = context.getSource();
        AnnounceViewActiveDTO destination = context.getDestination();
        if (destination == null) {
            destination = new AnnounceViewActiveDTO();
        }
        destination.setId(source.getAnnouncementId());
        destination.setAnnouncementTitle(source.getAnnouncementTitle());
        destination.setCategoriesObject(new CategoryDTO(source.getCategoriesObject().getCategoryName()));
        return destination;
    }
}
