package edu.rutmiit.demo.booksapicontract.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Проверяет, что строка является корректным 12-значным кодом UPC.
 * * Логика аналогична валидатору ISBN:
 * 1. Пропускает null/пустые значения (их проверяет @NotBlank).
 * 2. Очищает строку от пробелов и дефисов.
 * 3. Проверяет на соответствие регулярному выражению (12 цифр).
 */
public class UpcValidator implements ConstraintValidator<ValidUpc, String> {

    // UPC-A — это стандарт из 12 цифр
    private static final Pattern UPC_PATTERN = Pattern.compile("^\\d{12}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        // Убираем дефисы и пробелы перед проверкой
        String cleaned = value.replaceAll("[\\s\\-]", "");

        return UPC_PATTERN.matcher(cleaned).matches();
    }
}