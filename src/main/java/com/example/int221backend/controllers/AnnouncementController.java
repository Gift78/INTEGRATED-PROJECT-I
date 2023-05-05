package com.example.int221backend.controllers;

import com.example.int221backend.dtos.AnnounceDTO;
import com.example.int221backend.dtos.AnnounceDetailDTO;
import com.example.int221backend.dtos.AnnounceTestDTO;
import com.example.int221backend.entities.Announces;
import com.example.int221backend.services.AnnounceService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("")
    public List<AnnounceDTO> getAllAnnouncements() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Announces> announces = announceService.getAllAnnouncements();
        List<AnnounceDTO> announceDTOList = announces.stream().map(e -> {
            AnnounceDTO announceDTO = modelMapper.map(e, AnnounceDTO.class);
            announceDTO.setId(e.getAnnouncementId());
            return announceDTO;
        }).collect(Collectors.toList());
        return announceDTOList;
    }

    @GetMapping("{announceId}")
    public AnnounceDetailDTO getDetailById(@PathVariable Integer announceId) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Announces announcesExist = announceService.getAnnounceById(announceId);
        return modelMapper.map(announcesExist,AnnounceDetailDTO.class);
    }

    @PostMapping("")
    public AnnounceTestDTO create(@RequestBody Announces newAnnounce) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Announces announce = announceService.addNewAnnounce(newAnnounce);
        return modelMapper.map(announce, AnnounceTestDTO.class);
    }

    @DeleteMapping("{announceId}")
    public void removeAnnounce(@PathVariable Integer announceId){
        announceService.removeAnnounce((announceId));
    }

    @PutMapping("{announceId}")
    public AnnounceTestDTO updateAnnounce(@PathVariable Integer announceId, @RequestBody Announces newAnnounce){
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Announces announce = announceService.updateAnnounce(announceId, newAnnounce);
        return modelMapper.map(announce, AnnounceTestDTO.class);
    }
}
