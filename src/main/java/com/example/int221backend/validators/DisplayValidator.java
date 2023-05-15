package com.example.int221backend.validators;

import com.example.int221backend.entities.Announces;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DisplayValidator implements ConstraintValidator<ValidDisplay, Announces> {
    @Override
    public boolean isValid(Announces announce, ConstraintValidatorContext context) {
        if (announce.getAnnouncementDisplay() != null) {
            if (announce.getAnnouncementDisplay().equals("Y") || announce.getAnnouncementDisplay().equals("N")) {
                return true;
            }

            addConstraintViolation("must be either 'Y' or 'N'", "announcementDisplay", context);
        }

        if (announce.getAnnouncementDisplay() == null) {
            announce.setAnnouncementDisplay("N");
            return true;
        }

        return false;
    }

    public void addConstraintViolation(String messageTemplate, String field, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}