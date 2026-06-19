package edu.rutmiit.demo.booksapicontract.dto.Patch;

import edu.rutmiit.demo.booksapicontract.validation.ValidUpc;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Запрос для частичного обновления релиза (PATCH).
 *
 * Все поля необязательны. Передаются только те данные, которые изменились.
 * Поля, не указанные в JSON (null), сервер не трогает.
 */
@Schema(description = "Частичное обновление релиза (PATCH). Передайте только те поля, которые нужно изменить.")
public record PatchReleaseRequest(

        @Schema(description = "Название релиза", example = "Night Drive")
        String title,

        @Schema(description = "Уникальный штрих-код релиза (UPC-A, 12 цифр)", example = "123456789012")
        @ValidUpc
        String upc,

        @Schema(description = "Дата релиза", example = "ДД.ММ.ГГГГ")
        String dateOfRelease,

        @Schema(description = "Жанр", example = "Alternative")
        String genre,

        @Schema(description = "Продолжительность", example = "1:23")
        String duration,

        @Schema(description = "Обложка")
        String cover
) {}
