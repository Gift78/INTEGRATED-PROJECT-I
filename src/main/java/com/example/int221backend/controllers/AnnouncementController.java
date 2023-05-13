package com.example.int221backend.controllers;

import com.example.int221backend.converters.*;
import com.example.int221backend.dtos.*;
import com.example.int221backend.entities.Announces;
import com.example.int221backend.services.AnnounceService;
import com.example.int221backend.utils.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins= {"http://localhost:5173", "http://intproj22.sit.kmutt.ac.th", "http://127.0.0.1:5173"})
@RequestMapping("/api/announcements")
public class AnnouncementController {
    @Autowired
    private AnnounceService announceService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("")
    public List<?> getAllAnnouncements(@RequestParam(required = false) String mode) {
        List<Announces> announces = announceService.getAllAnnouncements(mode);
        if (mode == null || mode.equals("admin")) {
            modelMapper.addConverter(new AnnouncesToAnnounceDTOConverter());
            return announces.stream().map(e -> modelMapper.map(e, AnnounceDTO.class)).collect(Collectors.toList());
        } else if (mode.equals("active")) {
            modelMapper.addConverter(new AnnouncesToAnnounceViewActiveDTOConverter());
            return announces.stream().map(e -> modelMapper.map(e, AnnounceViewActiveDTO.class)).collect(Collectors.toList());
        } else if (mode.equals("close")) {
            modelMapper.addConverter(new AnnouncesToAnnounceViewCloseDTOConverter());
            return announces.stream().map(e -> modelMapper.map(e, AnnounceViewCloseDTO.class)).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @GetMapping("{announceId}")
    public AnnounceDetailDTO getDetailById(@PathVariable Integer announceId) {
        Announces announcesExist = announceService.getAnnounceById(announceId);
        modelMapper.addConverter(new AnnouncesToAnnounceDetailDTOConverter());
        return modelMapper.map(announcesExist, AnnounceDetailDTO.class);
    }

    @PostMapping("")
    public AnnounceDetailDTO create(@RequestBody Announces newAnnounce) {
        Announces announce = announceService.addNewAnnounce(newAnnounce);
        modelMapper.addConverter(new AnnouncesToAnnounceDetailDTOConverter());
        return modelMapper.map(announce, AnnounceDetailDTO.class);
    }

    @DeleteMapping("{announceId}")
    public void removeAnnounce(@PathVariable Integer announceId){
        announceService.removeAnnounce((announceId));
    }

    @PutMapping("{announceId}")
    public AnnounceTestDTO updateAnnounce(@PathVariable Integer announceId, @RequestBody Announces newAnnounce){
        Announces announce = announceService.updateAnnounce(announceId, newAnnounce);
        modelMapper.addConverter(new AnnouncesToAnnounceTestDTOConverter());
        return modelMapper.map(announce, AnnounceTestDTO.class);
    }

    @GetMapping("/pages")
    public PageDTO<AnnounceDTO> getAnnouncePage(@RequestParam(defaultValue = "admin") String mode,
                                                @RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "5") Integer size) {
        Page<Announces> announces = announceService.getAnnouncePage(mode, page, size);
        modelMapper.addConverter(new AnnouncesToAnnounceDTOConverter());
        PageDTO<AnnounceDTO> pageDTO = listMapper.toPageDTO(announces, AnnounceDTO.class, modelMapper);
        pageDTO.setPage(page);
        return pageDTO;
    }
}
