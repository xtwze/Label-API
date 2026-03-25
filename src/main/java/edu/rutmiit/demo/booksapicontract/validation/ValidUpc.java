package edu.rutmiit.demo.booksapicontract.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Аннотация для валидации UPC (Universal Product Code) музыкального релиза.
 * * Стандартный UPC-A состоит из 12 цифр.
 * Дефисы и пробелы допустимы — они игнорируются при проверке.
 */
@Documented
@Constraint(validatedBy = UpcValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUpc {

    String message() default "Некорректный UPC. Формат должен содержать ровно 12 цифр.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}