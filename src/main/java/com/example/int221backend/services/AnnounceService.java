package com.example.int221backend.services;

import com.example.int221backend.entities.Announces;
import com.example.int221backend.entities.Categories;
import com.example.int221backend.exceptions.AnnounceNotFoundException;
import com.example.int221backend.repositories.AnnounceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AnnounceService {
    @Autowired
    private AnnounceRepository announceRepository;

    @Autowired
    private CategoryService categoryService;

    private void datetimeFormatter(DateTimeFormatter localFormatter, Announces announce) {
        if (announce.getPublishDate() != null) {
            LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), localFormatter);
            ZonedDateTime zonePublishDate = localPublishDate.atZone(ZoneId.of("UTC"));
            Instant instantPublishDate = zonePublishDate.toInstant();
            announce.setPublishDate(instantPublishDate.toString());
        }
        if (announce.getCloseDate() != null) {
            LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), localFormatter);
            ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
            Instant instantCloseDate = zoneCloseDate.toInstant();
            announce.setCloseDate(instantCloseDate.toString());
        }
    }

    public List<Announces> getAllAnnouncements() {
        List<Announces> announces = announceRepository.findAll(Sort.by("announcementId").descending());
        announces.forEach((announce) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            datetimeFormatter(formatter, announce);
        });
        return announces;
    }

    public Announces getAnnounceById(Integer announceId) {
        Announces announce = announceRepository.findById(announceId).orElseThrow(() -> new AnnounceNotFoundException(announceId));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        datetimeFormatter(formatter, announce);
        return announce;
    }

    public Announces addNewAnnounce(Announces newAnnounce) {
        Categories category = categoryService.getCategoryById(newAnnounce.getCategoryId());
        newAnnounce.setCategoriesObject(category);

        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeFormatter localFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (newAnnounce.getPublishDate() != null) {
            LocalDateTime localDateTime = LocalDateTime.parse(newAnnounce.getPublishDate(), utcFormatter).atZone(ZoneId.of("UTC")).toLocalDateTime();
            newAnnounce.setPublishDate(localDateTime.format(localFormatter));
        }
        if (newAnnounce.getCloseDate() != null) {
            LocalDateTime localDateTime = LocalDateTime.parse(newAnnounce.getCloseDate(), utcFormatter).atZone(ZoneId.of("UTC")).toLocalDateTime();
            newAnnounce.setCloseDate(localDateTime.format(localFormatter));
        }
        Announces announce = announceRepository.saveAndFlush(newAnnounce);
        datetimeFormatter(localFormatter, announce);
        return announce;
    }

    public void removeAnnounce(Integer announceId){
        Announces announce = announceRepository.findById(announceId).orElseThrow(() -> new AnnounceNotFoundException(announceId));
        announceRepository.delete(announce);
    }

    public Announces updateAnnounce(Integer announceId, Announces newAnnounce) {
        Announces announce = announceRepository.findById(announceId).orElseThrow(() -> new AnnounceNotFoundException(announceId));

        Categories category = categoryService.getCategoryById(newAnnounce.getCategoryId());
        newAnnounce.setCategoriesObject(category);

        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeFormatter localFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (newAnnounce.getPublishDate() != null) {
            LocalDateTime localDateTime = LocalDateTime.parse(newAnnounce.getPublishDate(), utcFormatter).atZone(ZoneId.of("UTC")).toLocalDateTime();
            newAnnounce.setPublishDate(localDateTime.format(localFormatter));
        }
        if (newAnnounce.getCloseDate() != null) {
            LocalDateTime localDateTime = LocalDateTime.parse(newAnnounce.getCloseDate(), utcFormatter).atZone(ZoneId.of("UTC")).toLocalDateTime();
            newAnnounce.setCloseDate(localDateTime.format(localFormatter));
        }

        announce.setAnnouncementTitle(newAnnounce.getAnnouncementTitle());
        announce.setAnnouncementDescription(newAnnounce.getAnnouncementDescription());
        announce.setAnnouncementDisplay(newAnnounce.getAnnouncementDisplay());
        announce.setPublishDate(newAnnounce.getPublishDate());
        announce.setCloseDate(newAnnounce.getCloseDate());
        announce.setCategoriesObject(newAnnounce.getCategoriesObject());

        Announces announceSave = announceRepository.saveAndFlush(announce);
        datetimeFormatter(localFormatter, announceSave);
        return announceSave;
    }
}