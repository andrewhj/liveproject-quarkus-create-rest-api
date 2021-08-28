package io.chillplus.control;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueConstraintValidator.class)
@Documented
public @interface Unique {
    String message() default "{io.chillplus.service.unique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();

}
