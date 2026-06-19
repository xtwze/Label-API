package edu.rutmiit.demo.labelrest.exception;

import edu.rutmiit.demo.booksapicontract.dto.Responses.ErrorResponse;
import edu.rutmiit.demo.booksapicontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.booksapicontract.exception.UpcAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

/**
 * Глобальный перехватчик исключений. Все ошибки приводятся к единому формату
 * ErrorResponse (по мотивам RFC 7807 Problem Details), чтобы клиент (бот/фронт)
 * всегда получал предсказуемую структуру независимо от типа ошибки.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_PROBLEM_URI = "https://api.music-service.ru/problems/";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        404,
                        BASE_PROBLEM_URI + "resource-not-found",
                        "Ресурс не найден",
                        ex.getMessage(),
                        req.getRequestURI(),
                        Instant.now(),
                        null
                ));
    }

    @ExceptionHandler(UpcAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUpcConflict(UpcAlreadyExistsException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        409,
                        BASE_PROBLEM_URI + "upc-conflict",
                        "Конфликт UPC",
                        ex.getMessage(),
                        req.getRequestURI(),
                        Instant.now(),
                        null
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldError(
                        fe.getField(),
                        fe.getRejectedValue(),
                        fe.getDefaultMessage()
                ))
                .toList();

        String detail = fieldErrors.stream()
                .map(fe -> fe.field() + ": " + fe.message())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Ошибка валидации запроса");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        400,
                        BASE_PROBLEM_URI + "validation-error",
                        "Ошибка валидации",
                        detail,
                        req.getRequestURI(),
                        Instant.now(),
                        fieldErrors
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        500,
                        BASE_PROBLEM_URI + "internal-error",
                        "Внутренняя ошибка сервера",
                        "Произошла непредвиденная ошибка. Обратитесь к поддержке.",
                        req.getRequestURI(),
                        Instant.now(),
                        null
                ));
    }
}
