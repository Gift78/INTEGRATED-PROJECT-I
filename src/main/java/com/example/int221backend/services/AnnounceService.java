package com.example.int221backend.services;

import com.example.int221backend.entities.Announces;
import com.example.int221backend.exceptions.AnnounceNotFoundException;
import com.example.int221backend.repositories.AnnounceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

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

    public List<Announces> getAllAnnouncements() {
        List<Announces> announces = announceRepository.findAll(Sort.by("announcementId").descending());
        announces.forEach((announce) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (announce.getPublishDate() != null) {
                LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), formatter);
                ZonedDateTime zonePublishDate = localPublishDate.atZone(ZoneId.of("UTC"));
                Instant instantPublishDate = zonePublishDate.toInstant();
                announce.setPublishDate(instantPublishDate.toString());
            }
            if (announce.getCloseDate() != null) {
                LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                Instant instantCloseDate = zoneCloseDate.toInstant();
                announce.setCloseDate(instantCloseDate.toString());
            }
        });
        return announces;
    }

    public Announces getAnnounceById(Integer announceId) {
        Announces announce = announceRepository.findById(announceId).orElseThrow(() -> new AnnounceNotFoundException(announceId));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (announce.getPublishDate() != null) {
            LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), formatter);
            ZonedDateTime zonePublishDate = localPublishDate.atZone(ZoneId.of("UTC"));
            Instant instantPublishDate = zonePublishDate.toInstant();
            announce.setPublishDate(instantPublishDate.toString());
        }
        if (announce.getCloseDate() != null) {
            LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
            ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
            Instant instantCloseDate = zoneCloseDate.toInstant();
            announce.setCloseDate(instantCloseDate.toString());
        }
        return announce;
    }

    public Announces addNewAnnounce(Announces newAnnounce) {
        return announceRepository.saveAndFlush(newAnnounce);
    }

    public void removeAnnounce(Integer announceId){
        Announces announce =announceRepository.findById(announceId).orElseThrow(() -> new AnnounceNotFoundException(announceId));
        announceRepository.delete(announce);
    }

    public Announces updateAnnounce(Integer announceId, Announces newAnnounce) {
        Announces announce = announceRepository.findById(announceId).orElseThrow(() -> new AnnounceNotFoundException(announceId));

        announce.setAnnouncementTitle(newAnnounce.getAnnouncementTitle());
        announce.setAnnouncementDescription(newAnnounce.getAnnouncementDescription());
        announce.setAnnouncementDisplay(newAnnounce.getAnnouncementDisplay());
        announce.setCategoriesObject(newAnnounce.getCategoriesObject());
        announce.setPublishDate(newAnnounce.getPublishDate());
        announce.setCloseDate(newAnnounce.getCloseDate());
        return announceRepository.saveAndFlush(announce);
    }
}