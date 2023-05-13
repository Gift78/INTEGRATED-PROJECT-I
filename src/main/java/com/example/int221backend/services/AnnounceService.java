package com.example.int221backend.services;

import com.example.int221backend.entities.AnnouncementDisplay;
import com.example.int221backend.entities.Announces;
import com.example.int221backend.entities.Categories;
import com.example.int221backend.exceptions.AnnounceNotFoundException;
import com.example.int221backend.repositories.AnnounceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        List<Announces> announces = null;
        if (mode == null || mode.equals("admin")) {
            announces = announceRepository.findAllByOrderByAnnouncementIdDesc();
        } else if (mode.equals("active")) {
            announces = announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNull(AnnouncementDisplay.Y);
            List<Announces> announces1 = announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNullAndPublishDateBefore(AnnouncementDisplay.Y, String.valueOf(Instant.now()));
            List<Announces> announces2 = announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNotNullAndPublishDateBeforeAndCloseDateAfter(AnnouncementDisplay.Y, String.valueOf(Instant.now()), String.valueOf(Instant.now()));
            List<Announces> announces3 = announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNotNullAndCloseDateAfter(AnnouncementDisplay.Y, String.valueOf(Instant.now()));
            announces.addAll(announces1);
            announces.addAll(announces2);
            announces.addAll(announces3);

            // remove the duplicate
            Set<Announces> set = new HashSet<>(announces);
            announces.clear();
            announces.addAll(set);

            // sort the list by announcementId desc
            announces.sort(Comparator.comparing(Announces::getAnnouncementId).reversed());
        } else if (mode.equals("close")) {
            announces = announceRepository.findAllByAnnouncementDisplayAndCloseDateIsNotNullAndCloseDateBeforeOrderByAnnouncementIdDesc(AnnouncementDisplay.Y, String.valueOf(Instant.now()));
        }
        announces.forEach((announce) -> datetimeFormatter(formatter, announce));
        return announces;
    }



    public Announces getAnnounceById(Integer announceId) {
        Announces announce = announceRepository.findById(announceId).orElseThrow(() -> new AnnounceNotFoundException(announceId));
        datetimeFormatter(formatter, announce);
        return announce;
    }

    public Announces addNewAnnounce(Announces newAnnounce) {
        if (Objects.equals(newAnnounce.getPublishDate(), "")) {
            newAnnounce.setPublishDate(null);
        }

        if (Objects.equals(newAnnounce.getCloseDate(), "")) {
            newAnnounce.setCloseDate(null);
        }

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

    public void removeAnnounce(Integer announceId) {
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

    public Page<Announces> getAnnouncePage(String mode, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "announcementId");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Announces> announces;
        List<Announces> filteredAnnounces = new ArrayList<>();

        if (mode == null || mode.equals("admin")) {
            size = 9;
            pageable = PageRequest.of(page, size, sort);
            announces = announceRepository.findAll(pageable);
            for (Announces announce : announces) {
                datetimeFormatter(formatter, announce);
            }
            return announces;
        } else if (mode.equals("active")) {
            List<Announces> test = announceRepository.findAll();
            for (Announces announce : test) {
                if (String.valueOf(announce.getAnnouncementDisplay()).equals("Y")) {
                    if (announce.getPublishDate() != null && announce.getCloseDate() == null) {
                        LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), formatter);
                        ZonedDateTime zonePublishDate = localPublishDate.atZone(ZoneId.of("UTC"));
                        Instant instantPublishDate = zonePublishDate.toInstant();
                        if (Instant.now().compareTo(instantPublishDate) >= 0) {
                            filteredAnnounces.add(announce);
                        }
                    } else if (announce.getPublishDate() != null && announce.getCloseDate() != null) {
                        LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), formatter);
                        ZonedDateTime zonePublishDate = localPublishDate.atZone(ZoneId.of("UTC"));
                        Instant instantPublishDate = zonePublishDate.toInstant();
                        LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                        ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                        Instant instantCloseDate = zoneCloseDate.toInstant();
                        if (Instant.now().compareTo(instantPublishDate) >= 0 && Instant.now().compareTo(instantCloseDate) < 0) {
                            filteredAnnounces.add(announce);
                        }
                    } else if (announce.getPublishDate() == null && announce.getCloseDate() != null) {
                        LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                        ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                        Instant instantCloseDate = zoneCloseDate.toInstant();
                        if (Instant.now().compareTo(instantCloseDate) < 0) {
                            filteredAnnounces.add(announce);
                        }
                    } else if (announce.getPublishDate() == null && announce.getCloseDate() == null) {
                        filteredAnnounces.add(announce);
                    }
                }
            }
        } else if (mode.equals("close")) {
            List<Announces> test = announceRepository.findAll();
            for (Announces announce : test) {
                if (String.valueOf(announce.getAnnouncementDisplay()).equals("Y")) {
                    if (announce.getCloseDate() != null) {
                        LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                        ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                        Instant instantCloseDate = zoneCloseDate.toInstant();
                        if (Instant.now().compareTo(instantCloseDate) >= 0) {
                            filteredAnnounces.add(announce);
                        }
                    }
                }
            }
        }

        for (Announces announce : filteredAnnounces) {
            datetimeFormatter(formatter, announce);
        }

        filteredAnnounces.sort(Comparator.comparing(Announces::getAnnouncementId).reversed());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredAnnounces.size());
        return new PageImpl<>(filteredAnnounces.subList(start, end), pageable, filteredAnnounces.size());
    }

    public Page<Announces> getAnnounceByCategoryId(String mode, Integer page, Integer size, Integer catId) {
//        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"announcementId"));
//        return announceRepository.findAllByCategoryId(catId,pageable);
        Sort sort = Sort.by(Sort.Direction.DESC, "announcementId");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Announces> announces;
        List<Announces> filteredAnnounces = new ArrayList<>();

        if (mode == null || mode.equals("admin")) {
            size = 9;
            pageable = PageRequest.of(page, size, sort);
            announces = announceRepository.findAllByCategoryId(catId,pageable);
            for (Announces announce : announces) {
                datetimeFormatter(formatter, announce);
            }
            return announces;
        } else if (mode.equals("active")) {
            List<Announces> test = announceRepository.findAllByCategoryId(catId);
            for (Announces announce : test) {
                if (String.valueOf(announce.getAnnouncementDisplay()).equals("Y")) {
                    if (announce.getPublishDate() != null && announce.getCloseDate() == null) {
                        LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), formatter);
                        ZonedDateTime zonePublishDate = localPublishDate.atZone(ZoneId.of("UTC"));
                        Instant instantPublishDate = zonePublishDate.toInstant();
                        if (Instant.now().compareTo(instantPublishDate) >= 0) {
                            filteredAnnounces.add(announce);
                        }
                    } else if (announce.getPublishDate() != null && announce.getCloseDate() != null) {
                        LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), formatter);
                        ZonedDateTime zonePublishDate = localPublishDate.atZone(ZoneId.of("UTC"));
                        Instant instantPublishDate = zonePublishDate.toInstant();
                        LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                        ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                        Instant instantCloseDate = zoneCloseDate.toInstant();
                        if (Instant.now().compareTo(instantPublishDate) >= 0 && Instant.now().compareTo(instantCloseDate) < 0) {
                            filteredAnnounces.add(announce);
                        }
                    } else if (announce.getPublishDate() == null && announce.getCloseDate() != null) {
                        LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                        ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                        Instant instantCloseDate = zoneCloseDate.toInstant();
                        if (Instant.now().compareTo(instantCloseDate) < 0) {
                            filteredAnnounces.add(announce);
                        }
                    } else if (announce.getPublishDate() == null && announce.getCloseDate() == null) {
                        filteredAnnounces.add(announce);
                    }
                }
            }
        } else if (mode.equals("close")) {
            List<Announces> test = announceRepository.findAllByCategoryId(catId);
            for (Announces announce : test) {
                if (String.valueOf(announce.getAnnouncementDisplay()).equals("Y")) {
                    if (announce.getCloseDate() != null) {
                        LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                        ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                        Instant instantCloseDate = zoneCloseDate.toInstant();
                        if (Instant.now().compareTo(instantCloseDate) >= 0) {
                            filteredAnnounces.add(announce);
                        }
                    }
                }
            }
        }

        for (Announces announce : filteredAnnounces) {
            datetimeFormatter(formatter, announce);
        }

        filteredAnnounces.sort(Comparator.comparing(Announces::getAnnouncementId).reversed());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredAnnounces.size());
        return new PageImpl<>(filteredAnnounces.subList(start, end), pageable, filteredAnnounces.size());
    }
}