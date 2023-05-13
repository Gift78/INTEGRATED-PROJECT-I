package com.example.int221backend.repositories;

import com.example.int221backend.entities.AnnouncementDisplay;
import com.example.int221backend.entities.Announces;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnounceRepository extends JpaRepository<Announces, Integer> {
    List<Announces> findAllByOrderByAnnouncementIdDesc();
    List<Announces> findAllByAnnouncementDisplayAndCloseDateIsNotNullAndCloseDateBeforeOrderByAnnouncementIdDesc(AnnouncementDisplay announcementDisplay, String date);
    List<Announces> findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNull(AnnouncementDisplay announcementDisplay);
    List<Announces> findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNullAndPublishDateBefore(AnnouncementDisplay announcementDisplay, String date);
    List<Announces> findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNotNullAndPublishDateBeforeAndCloseDateAfter(AnnouncementDisplay announcementDisplay, String date1, String date2);
    List<Announces> findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNotNullAndCloseDateAfter(AnnouncementDisplay announcementDisplay, String date);

    Page<Announces> findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNull(AnnouncementDisplay announcementDisplay, Pageable pageable);

    Page<Announces> findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNullAndPublishDateBefore(AnnouncementDisplay announcementDisplay, String s, Pageable pageable);

    Page<Announces> findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNotNullAndCloseDateAfter(AnnouncementDisplay announcementDisplay, String s, Pageable pageable);

    Page<Announces> findAllByAnnouncementDisplayAndCloseDateIsNotNullAndCloseDateBeforeOrderByAnnouncementIdDesc(AnnouncementDisplay announcementDisplay, String s, Pageable pageable);

    Page<Announces> findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNotNullAndPublishDateBeforeAndCloseDateAfter(AnnouncementDisplay announcementDisplay, String s, String s1, Pageable pageable);

    Page<Announces> findAllByCategoryId(Integer catId,Pageable pageable);
    List<Announces> findAllByCategoryId(Integer catId);
}
