package com.example.int221backend.repositories;

import com.example.int221backend.entities.Announces;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AnnounceRepository extends JpaRepository<Announces, Integer> {
    List<Announces> findAllByAnnouncementDisplayAndCloseDateIsNotNullAndCloseDateBefore(String announcementDisplay, String closeDate);
    List<Announces> findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNull(String announcementDisplay);
    List<Announces> findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNullAndPublishDateBefore(String announcementDisplay, String publishDate);
    List<Announces> findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNotNullAndPublishDateBeforeAndCloseDateAfter(String announcementDisplay, String publishDate, String closeDate);
    List<Announces> findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNotNullAndCloseDateAfter(String announcementDisplay, String closeDate);

    Page<Announces> findAllByCategoryId(Integer catId,Pageable pageable);

    List<Announces> findAllByCategoryIdAndAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNull(Integer categoryId, String announcementDisplay);

    List<Announces> findAllByCategoryIdAndAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNullAndPublishDateBefore(Integer categoryId, String y, String s);

    List<Announces> findAllByCategoryIdAndAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNotNullAndPublishDateBeforeAndCloseDateAfter(Integer categoryId, String y, String s, String s1);

    List<Announces> findAllByCategoryIdAndAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNotNullAndCloseDateAfter(Integer categoryId, String y, String s);

    List<Announces> findAllByCategoryIdAndAnnouncementDisplayAndCloseDateIsNotNullAndCloseDateBefore(Integer categoryId, String y, String s);
}
