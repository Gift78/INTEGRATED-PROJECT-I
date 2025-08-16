package com.example.int221backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AnnounceNotFoundException extends RuntimeException {
    public AnnounceNotFoundException(Integer id) {
        super("Announcement id " + id + " does not exist!.");
    }
}
