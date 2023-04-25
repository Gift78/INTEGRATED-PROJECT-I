package com.example.int221backend.services;


import com.example.int221backend.entities.Announces;
import com.example.int221backend.repositories.AnnounceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnounceService {
    @Autowired
    private AnnounceRepository announceRepository;

    public List<Announces> getAllAnnouncements() {
        return announceRepository.findAll(Sort.by("announcementId").descending());
    }

    public Announces getAnnounceById(Integer announceId) {
        return announceRepository.findById(announceId).orElseThrow(
                () -> new RuntimeException("Announcement ID " + announceId + " does not exist!."));
    }

}
