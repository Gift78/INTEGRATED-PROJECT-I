package com.example.int221backend.validators;

import com.example.int221backend.entities.Announces;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PublishAndCloseDateValidator implements ConstraintValidator<ValidPublishAndCloseDate, Announces> {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    @Override
    public boolean isValid(Announces announces, ConstraintValidatorContext context) {

        String publishDate = announces.getPublishDate();
        String closeDate = announces.getCloseDate();

        if (publishDate == null && closeDate == null) {
            return true;
        }

        if (publishDate != null && closeDate != null) {
            LocalDateTime publishDateTime = LocalDateTime.parse(publishDate, utcFormatter).atZone(ZoneId.of("UTC")).toLocalDateTime();
            LocalDateTime closeDateTime = LocalDateTime.parse(closeDate, utcFormatter).atZone(ZoneId.of("UTC")).toLocalDateTime();

            if (publishDateTime.isAfter(closeDateTime)) {
                addErrorMessage(context, "publishDate", "Publish date must be before close date");
                return false;
            }
        }

        if (closeDate != null) {
            LocalDateTime closeDateTime = LocalDateTime.parse(closeDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
            if (closeDateTime.isBefore(LocalDateTime.now())) {
                addErrorMessage(context, "closeDate", "Close date must be a future date");
                return false;
            }
        }

        return true;
    }

    private void addErrorMessage(ConstraintValidatorContext context, String field, String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }
}

