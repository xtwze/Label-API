package edu.rutmiit.demo.booksapicontract.dto.Responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

/**
 * Стандартный ответ об ошибке по формату RFC 7807 Problem Details.
 * Используется для передачи ошибок от Web-приложения (Сервер Б) к Боту (Сервер А).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Стандартный ответ об ошибке (RFC 7807 Problem Details)")
public record ErrorResponse(

        @Schema(description = "HTTP статус-код", example = "400")
        int status,

        @Schema(
                description = "URI-идентификатор типа ошибки (машиночитаемый)",
                example = "https://api.music-service.ru/problems/validation-error"
        )
        String type,

        @Schema(description = "Краткое название типа ошибки", example = "Ошибка валидации данных релиза")
        String title,

        @Schema(description = "Детальное описание ошибки", example = "Невозможно создать договор: не заполнены паспортные данные артиста")
        String detail,

        @Schema(description = "URI запроса, приведшего к ошибке", example = "/api/v1/releases")
        String instance,

        @Schema(description = "Момент возникновения ошибки (UTC)", example = "2024-05-20T12:00:00Z")
        Instant timestamp,

        @Schema(description = "Список ошибок по конкретным полям релиза (для 400 Bad Request)")
        List<FieldError> fieldErrors
) {

    /**
     * Ошибка валидации конкретного поля (например, некорректная дата или пустой жанр).
     */
    @Schema(description = "Ошибка валидации поля")
    public record FieldError(

            @Schema(description = "Имя поля из ReleaseRequest", example = "dateOfRelease")
            String field,

            @Schema(description = "Значение, которое было отклонено", example = "вчера")
            Object rejectedValue,

            @Schema(description = "Сообщение об ошибке", example = "Дата релиза не может быть в прошлом")
            String message
    ) {}
}