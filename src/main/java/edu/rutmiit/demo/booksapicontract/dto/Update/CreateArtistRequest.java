package edu.rutmiit.demo.booksapicontract.dto.Update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Запрос на создание артиста (POST).
 *
 * Email и Telegram ID необязательны при создании,
 * но могут потребоваться для верификации позже.
 */
@Schema(description = "Создание артиста (POST). Базовая информация обязательна.")
public record CreateArtistRequest(

        @Schema(description = "Имя артиста", example = "Михаил", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя не может быть пустым")
        @Size(min = 2, max = 100, message = "Имя должно содержать 2-100 символов")
        String firstName,

        @Schema(description = "Фамилия артиста", example = "Константинов", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Фамилия не может быть пустой")
        @Size(min = 2, max = 100, message = "Фамилия должна содержать 2-100 символов")
        String lastName,

        @Schema(description = "Email артиста", example = "konstantinov@example.com")
        @Email(message = "Некорректный формат email")
        @Size(max = 255, message = "Email не может превышать 255 символов")
        String email,

        @Schema(description = "Никнейм артиста", example = "xtwze")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Никнейм содержит только буквы, цифры и подчеркивания")
        @Size(max = 55, message = "Никнейм не может превышать 55 символов")
        String nickname,

        @Schema(description = "Telegram ID артиста", example = "123456789")
        @Pattern(regexp = "\\d{1,20}", message = "Telegram ID содержит только цифры, максимум 20")
        @Size(max = 20, message = "Telegram ID не может превышать 20 символов")
        String telegramUserId
) {}