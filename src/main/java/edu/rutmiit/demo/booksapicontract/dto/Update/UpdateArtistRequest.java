package edu.rutmiit.demo.booksapicontract.dto.Update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Запрос на полное обновление данных артиста (PUT).
 *
 * В отличие от {@code CreateArtistRequest}, здесь обязательны все поля,
 * включая те, что при создании были опциональными (email, никнейм, telegramUserId).
 * Семантика PUT — полная замена ресурса: поля, не переданные в запросе,
 * считаются ошибкой валидации, а не "не изменять".
 */
@Schema(description = "Полное обновление данных артиста (PUT). Все поля обязательны.")
public record UpdateArtistRequest(

        @Schema(description = "Имя артиста", example = "Михаил", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя не может быть пустым")
        @Size(min = 2, max = 100, message = "Имя должно содержать 2-100 символов")
        String firstName,

        @Schema(description = "Фамилия артиста", example = "Константинов", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Фамилия не может быть пустой")
        @Size(min = 2, max = 100, message = "Фамилия должна содержать 2-100 символов")
        String lastName,

        @Schema(description = "Email артиста", example = "konstantinov@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректный формат email")
        @Size(max = 255, message = "Email не может превышать 255 символов")
        String email,

        @Schema(description = "Никнейм артиста", example = "xtwze", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Никнейм не может быть пустым")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Никнейм содержит только буквы, цифры и подчеркивания")
        @Size(max = 55, message = "Никнейм не может превышать 55 символов")
        String nickname,

        @Schema(description = "Telegram ID артиста", example = "123456789", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Telegram ID не может быть пустым")
        @Pattern(regexp = "\\d{1,20}", message = "Telegram ID содержит только цифры, максимум 20")
        String telegramUserId
) {}
