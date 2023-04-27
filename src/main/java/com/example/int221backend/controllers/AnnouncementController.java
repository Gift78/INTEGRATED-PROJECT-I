package com.example.int221backend.controllers;

import com.example.int221backend.entities.Announces;
import com.example.int221backend.services.AnnounceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    @Autowired
    private AnnounceService announceService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public List<Announces> getAllAnnouncements() {
        return announceService.getAllAnnouncements();
    }

    @GetMapping("{announceId}")
    public Announces getDetailById(@PathVariable Integer announceId){
        return announceService.getAnnounceById(announceId);
    }
}
