package com.example.int221backend.converters;

import com.example.int221backend.dtos.AnnounceViewCloseDTO;
import com.example.int221backend.dtos.CategoryDTO;
import com.example.int221backend.entities.Announces;
import org.springframework.stereotype.Component;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
@Component
public class AnnouncesToAnnounceViewCloseDTOConverter implements Converter<Announces, AnnounceViewCloseDTO> {
    @Override
    public AnnounceViewCloseDTO convert (MappingContext<Announces, AnnounceViewCloseDTO> context) {
        Announces source = context.getSource();
        AnnounceViewCloseDTO destination = context.getDestination();
        if (destination == null) {
            destination = new AnnounceViewCloseDTO();
        }
        destination.setId(source.getAnnouncementId());
        destination.setAnnouncementTitle(source.getAnnouncementTitle());
        destination.setCloseDate(source.getCloseDate());
        destination.setCategoriesObject(new CategoryDTO(source.getCategoriesObject().getCategoryName()));
        return destination;
    }
}
