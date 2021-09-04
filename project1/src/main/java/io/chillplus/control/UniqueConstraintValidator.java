package io.chillplus.control;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// TODO: maybe we should put this off and just do a unique id check within the resource endpoint.
public class UniqueConstraintValidator implements ConstraintValidator<Unique, String> {
    String fieldName;

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return false;
    }

    @Override
    public void initialize(final Unique constraintAnnotation) {
        this.fieldName = constraintAnnotation.value();
    }
}
