package com.example.int221backend.controllers;

import com.example.int221backend.dtos.AnnounceDTO;
import com.example.int221backend.dtos.AnnounceDetailDTO;
import com.example.int221backend.entities.Announces;
import com.example.int221backend.entities.Categories;
import com.example.int221backend.services.AnnounceService;
import com.example.int221backend.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/announcements")
public class AnnouncementController {
    @Autowired
    private AnnounceService announceService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("")
    public List<AnnounceDTO> getAllAnnouncements() {
        List<Announces> announces = announceService.getAllAnnouncements();
        List<AnnounceDTO> announceDTOList = announces.stream().map(e -> modelMapper.map(e, AnnounceDTO.class)).collect(Collectors.toList());
        return announceDTOList;
    }

    @GetMapping("{announceId}")
    public AnnounceDetailDTO getDetailById(@PathVariable Integer announceId) {
        Announces announcesExist = announceService.getAnnounceById(announceId);
        return modelMapper.map(announcesExist,AnnounceDetailDTO.class);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Announces create(@RequestBody Announces newAnnounce) { //แปลง json ให้เป็น object ของ java โดย @RequestBody
        return announceService.addNewAnnounce(newAnnounce);
    }

    @GetMapping("/category")
    public List<Categories> getCategories(){
        return categoryService.getAllCategory();
    }
}
