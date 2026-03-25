package edu.rutmiit.demo.booksapicontract.dto.Patch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


/**
 * Запрос для частичного обновления данных артиста (PATCH).
 * * Все поля необязательны. Передаются только те данные, которые изменились.
 * Поля, не указанные в JSON (null), сервер не трогает.
 */
@Schema(description = "Частичное обновление данных артиста (PATCH). Передайте только те поля, которые нужно изменить.")
public record PatchArtistRequest(

        @Schema(description = "Новое имя артиста", example = "Михаил")
        @Size(max = 100, message = "Имя не может превышать 100 символов")
        String firstName,

        @Schema(description = "Новая фамилия артиста", example = "Константинов")
        @Size(max = 100, message = "Фамилия не может превышать 100 символов")
        String lastName,

        @Schema(description = "Новый email для связи", example = "new_email@example.com")
        @Email(message = "Некорректный формат email")
        @Size(max = 255, message = "Email не может превышать 255 символов")
        String email,

        @Schema(description = "Новый никнейм артиста", example = "xtwze_new")
        @Size(max = 55, message = "Никнейм не может превышать 55 символов")
        String nickname
) {}