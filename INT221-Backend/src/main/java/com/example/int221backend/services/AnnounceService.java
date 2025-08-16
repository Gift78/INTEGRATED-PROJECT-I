package com.example.int221backend.services;

import com.example.int221backend.entities.Announces;
import com.example.int221backend.entities.Categories;
import com.example.int221backend.exceptions.AnnounceNotFoundException;
import com.example.int221backend.repositories.AnnounceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AnnounceService {
    @Autowired
    private AnnounceRepository announceRepository;
    @Autowired
    private CategoryService categoryService;
    private final DateTimeFormatter DATE_FORMATTER_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private final DateTimeFormatter DATE_FORMATTER_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final DateTimeFormatter DATE_FORMATTER_3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter DATE_FORMATTER_4 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private void datetimeFormatter(DateTimeFormatter dateTimeFormatter, Announces announce) {
        if (announce.getPublishDate() != null) {
            LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), dateTimeFormatter);
            Instant instantPublishDate = localPublishDate.toInstant(ZoneOffset.UTC);
            announce.setPublishDate(instantPublishDate.toString());
        }
        if (announce.getCloseDate() != null) {
            LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), dateTimeFormatter);
            Instant instantCloseDate = localCloseDate.toInstant(ZoneOffset.UTC);
            announce.setCloseDate(instantCloseDate.toString());
        }
    }

    public List<Announces> getAllAnnouncements(String mode) {
        List<Announces> announces = null;
        if (mode == null || mode.equals("admin")) {
            announces = announceRepository.findAll();
        } else if (mode.equals("active")) {
            announces.addAll(announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNull("Y"));
            announces.addAll(announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNullAndPublishDateBefore("Y", String.valueOf(Instant.now())));
            announces.addAll(announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNotNullAndPublishDateBeforeAndCloseDateAfter("Y", String.valueOf(Instant.now()), String.valueOf(Instant.now())));
            announces.addAll(announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNotNullAndCloseDateAfter("Y", String.valueOf(Instant.now())));

            Set<Announces> set = new HashSet<>(announces);
            announces.clear();
            announces.addAll(set);
        } else if (mode.equals("close")) {
            announces = announceRepository.findAllByAnnouncementDisplayAndCloseDateIsNotNullAndCloseDateBefore("Y", String.valueOf(Instant.now()));
        }

        announces.sort(Comparator.comparing(Announces::getAnnouncementId).reversed());
        announces.forEach((announce) -> datetimeFormatter(DATE_FORMATTER_3, announce));
        return announces;
    }

    public Announces getAnnounceById(Integer announceId, Boolean count) {
        Announces announce = announceRepository.findById(announceId).orElseThrow(() -> new AnnounceNotFoundException(announceId));

        if (count != null && count) {
            announce.setViewCount(announce.getViewCount() + 1);
            announceRepository.saveAndFlush(announce);
        }

        datetimeFormatter(DATE_FORMATTER_3, announce);
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
            if (newAnnounce.getPublishDate().length() == 24) {
                LocalDateTime localDateTime = LocalDateTime.parse(newAnnounce.getPublishDate(), DATE_FORMATTER_4);
                newAnnounce.setPublishDate(localDateTime.format(DATE_FORMATTER_1));
            }

            LocalDateTime localDateTime = LocalDateTime.parse(newAnnounce.getPublishDate(), DATE_FORMATTER_1).atZone(ZoneId.of("UTC")).toLocalDateTime();
            newAnnounce.setPublishDate(localDateTime.format(DATE_FORMATTER_2));
        }
        if (newAnnounce.getCloseDate() != null) {
            if (newAnnounce.getCloseDate().length() == 24) {
                LocalDateTime localDateTime = LocalDateTime.parse(newAnnounce.getCloseDate(), DATE_FORMATTER_4);
                newAnnounce.setCloseDate(localDateTime.format(DATE_FORMATTER_1));
            }

            LocalDateTime localDateTime = LocalDateTime.parse(newAnnounce.getCloseDate(), DATE_FORMATTER_1).atZone(ZoneId.of("UTC")).toLocalDateTime();
            newAnnounce.setCloseDate(localDateTime.format(DATE_FORMATTER_2));
        }

        Announces announce = announceRepository.saveAndFlush(newAnnounce);
        datetimeFormatter(DATE_FORMATTER_2, announce);
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
            if (newAnnounce.getPublishDate().length() == 24) {
                LocalDateTime localDateTime = LocalDateTime.parse(newAnnounce.getPublishDate(), DATE_FORMATTER_4);
                newAnnounce.setPublishDate(localDateTime.format(DATE_FORMATTER_1));
            }

            LocalDateTime localDateTime = LocalDateTime.parse(newAnnounce.getPublishDate(), DATE_FORMATTER_1).atZone(ZoneId.of("UTC")).toLocalDateTime();
            newAnnounce.setPublishDate(localDateTime.format(DATE_FORMATTER_2));
        }
        if (newAnnounce.getCloseDate() != null) {
            if (newAnnounce.getCloseDate().length() == 24) {
                LocalDateTime localDateTime = LocalDateTime.parse(newAnnounce.getCloseDate(), DATE_FORMATTER_4);
                newAnnounce.setCloseDate(localDateTime.format(DATE_FORMATTER_1));
            }

            LocalDateTime localDateTime = LocalDateTime.parse(newAnnounce.getCloseDate(), DATE_FORMATTER_1).atZone(ZoneId.of("UTC")).toLocalDateTime();
            newAnnounce.setCloseDate(localDateTime.format(DATE_FORMATTER_2));
        }

        announce.setAnnouncementTitle(newAnnounce.getAnnouncementTitle());
        announce.setAnnouncementDescription(newAnnounce.getAnnouncementDescription());
        announce.setAnnouncementDisplay(newAnnounce.getAnnouncementDisplay());
        announce.setPublishDate(newAnnounce.getPublishDate());
        announce.setCloseDate(newAnnounce.getCloseDate());
        announce.setCategoriesObject(newAnnounce.getCategoriesObject());

        Announces announceSave = announceRepository.saveAndFlush(announce);
        datetimeFormatter(DATE_FORMATTER_2, announceSave);
        return announceSave;
    }

    public Page<Announces> getAnnouncePage(String mode, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "announcementId");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Announces> filteredAnnounces = new ArrayList<>();

        if (mode == null || mode.equals("admin")) {
            Page<Announces> announces;
            announces = announceRepository.findAll(pageable);
            announces.forEach(announce -> datetimeFormatter(DATE_FORMATTER_3, announce));
            return announces;
        } else if (mode.equals("active")) {
            filteredAnnounces.addAll(announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNull("Y"));
            filteredAnnounces.addAll(announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNullAndPublishDateBefore("Y", String.valueOf(Instant.now())));
            filteredAnnounces.addAll(announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNotNullAndPublishDateBeforeAndCloseDateAfter("Y", String.valueOf(Instant.now()), String.valueOf(Instant.now())));
            filteredAnnounces.addAll(announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNotNullAndCloseDateAfter("Y", String.valueOf(Instant.now())));

            Set<Announces> set = new LinkedHashSet<>(filteredAnnounces);
            filteredAnnounces.clear();
            filteredAnnounces.addAll(set);
            filteredAnnounces.forEach(announce -> datetimeFormatter(DATE_FORMATTER_3, announce));
        } else if (mode.equals("close")) {
            filteredAnnounces.addAll(announceRepository.findAllByAnnouncementDisplayAndCloseDateIsNotNullAndCloseDateBefore("Y", String.valueOf(Instant.now())));
        }

        filteredAnnounces.sort(Comparator.comparing(Announces::getAnnouncementId).reversed());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredAnnounces.size());
        return new PageImpl<>(filteredAnnounces.subList(start, end), pageable, filteredAnnounces.size());
    }

    public Page<Announces> getAnnounceByCategoryId(String mode, Integer page, Integer size, Integer categoryId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "announcementId");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Announces> filteredAnnounces = new ArrayList<>();

        if (mode == null || mode.equals("admin")) {
            Page<Announces> announces;
            announces = announceRepository.findAllByCategoryId(categoryId, pageable);
            announces.forEach(announce -> datetimeFormatter(DATE_FORMATTER_3, announce));
            return announces;
        } else if (mode.equals("active")) {
            filteredAnnounces.addAll(announceRepository.findAllByCategoryIdAndAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNull(categoryId, "Y"));
            filteredAnnounces.addAll(announceRepository.findAllByCategoryIdAndAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNullAndPublishDateBefore(categoryId, "Y", String.valueOf(Instant.now())));
            filteredAnnounces.addAll(announceRepository.findAllByCategoryIdAndAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNotNullAndPublishDateBeforeAndCloseDateAfter(categoryId, "Y", String.valueOf(Instant.now()), String.valueOf(Instant.now())));
            filteredAnnounces.addAll(announceRepository.findAllByCategoryIdAndAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNotNullAndCloseDateAfter(categoryId, "Y", String.valueOf(Instant.now())));

            Set<Announces> set = new LinkedHashSet<>(filteredAnnounces);
            filteredAnnounces.clear();
            filteredAnnounces.addAll(set);

            filteredAnnounces.forEach(announce -> datetimeFormatter(DATE_FORMATTER_3, announce));
        } else if (mode.equals("close")) {
            filteredAnnounces.addAll(announceRepository.findAllByCategoryIdAndAnnouncementDisplayAndCloseDateIsNotNullAndCloseDateBefore(categoryId, "Y", String.valueOf(Instant.now())));
        }

        filteredAnnounces.sort(Comparator.comparing(Announces::getAnnouncementId).reversed());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredAnnounces.size());
        return new PageImpl<>(filteredAnnounces.subList(start, end), pageable, filteredAnnounces.size());
    }
}