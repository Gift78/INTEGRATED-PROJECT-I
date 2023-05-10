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
    private final DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private final DateTimeFormatter localFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    public List<Announces> getAllAnnouncements(String mode) {
        List<Announces> announces = announceRepository.findAll(Sort.by("announcementId").descending());

        if (mode == null || mode.equals("admin")) {
            announces.forEach((announce) -> datetimeFormatter(formatter, announce));
            return announces;
        } else if (mode.equals("active")) {
            announces.removeIf(announce -> String.valueOf(announce.getAnnouncementDisplay()).equals("N"));
            announces.removeIf((announce) -> {
               if (announce.getPublishDate() != null && announce.getCloseDate() == null) {
                    LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), formatter);
                    ZonedDateTime zonePublishDate = localPublishDate.atZone(ZoneId.of("UTC"));
                    Instant instantPublishDate = zonePublishDate.toInstant();
                    return Instant.now().compareTo(instantPublishDate) < 0;
                } else if (announce.getPublishDate() != null && announce.getCloseDate() != null) {
                    LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), formatter);
                    ZonedDateTime zonePublishDate = localPublishDate.atZone(ZoneId.of("UTC"));
                    Instant instantPublishDate = zonePublishDate.toInstant();
                    LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                    ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                    Instant instantCloseDate = zoneCloseDate.toInstant();
                    return Instant.now().compareTo(instantPublishDate) < 0 || Instant.now().compareTo(instantCloseDate) >= 0;
                } else if (announce.getPublishDate() == null && announce.getCloseDate() != null) {
                    LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                    ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                    Instant instantCloseDate = zoneCloseDate.toInstant();
                    return Instant.now().compareTo(instantCloseDate) >= 0;
                }
                return false;
            });
            announces.forEach(announce -> datetimeFormatter(formatter, announce));
            return announces;
        } else if (mode.equals("close")) {
            announces.removeIf(announce -> String.valueOf(announce.getAnnouncementDisplay()).equals("N"));
            announces.removeIf((announce) -> {
                if (announce.getCloseDate() == null) {
                    return true;
                } else {
                    LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                    ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                    Instant instantCloseDate = zoneCloseDate.toInstant();
                    return Instant.now().compareTo(instantCloseDate) < 0;
                }
            });
            announces.forEach((announce) -> datetimeFormatter(formatter, announce));
            return announces;
        } else {
            return null;
        }
    }

    public Announces getAnnounceById(Integer announceId) {
        Announces announce = announceRepository.findById(announceId).orElseThrow(() -> new AnnounceNotFoundException(announceId));
        datetimeFormatter(formatter, announce);
        return announce;
    }

    public Announces addNewAnnounce(Announces newAnnounce) {
        newAnnounce.setCategoriesObject(categoryService.getCategoryById(newAnnounce.getCategoryId()));

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