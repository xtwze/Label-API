package edu.rutmiit.demo.booksapicontract.dto.Update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Запрос для полного обновления профиля артиста (PUT).
 *
 * Все поля обязательны для обновления.
 * ID профиля и telegramUserId нельзя изменить.
 */
@Schema(description = "Полное обновление профиля артиста (PUT). Все поля обязательны. " +
        "ID профиля и Telegram ID не меняются.")
public record UpdateArtistProfileRequest(

        @Schema(description = "Имя", example = "Иван", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя не может быть пустым")
        @Size(min = 2, max = 100, message = "Имя должно содержать 2-100 символов")
        String firstName,

        @Schema(description = "Фамилия", example = "Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Фамилия не может быть пустой")
        @Size(min = 2, max = 100, message = "Фамилия должна содержать 2-100 символов")
        String lastName,

        @Schema(description = "Контактный Email", example = "artist@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Email должен быть корректным")
        @Size(max = 255, message = "Email не может превышать 255 символов")
        String email,

        @Schema(description = "Дата рождения", example = "1990-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Дата рождения обязательна")
        @Past(message = "Дата рождения должна быть в прошлом")
        java.time.LocalDate birthDate,

        @Schema(description = "Маскированный номер паспорта", example = "1234 ****90", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Номер паспорта не может быть пустым")
        @Pattern(regexp = "\\d{4}\\s\\*+\\d{2}",
                message = "Формат: 1234 ****90")
        String maskedPassport,

        @Schema(description = "ИНН", example = "123456789012", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "ИНН не может быть пустым")
        @Size(min = 12, max = 12, message = "ИНН должен содержать 12 цифр")
        @Pattern(regexp = "\\d{12}", message = "ИНН должен содержать только цифры")
        String inn,

        @Schema(description = "Адрес регистрации", example = "г. Москва, ул. Пушкина, д. 10", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Адрес не может быть пустым")
        @Size(max = 500, message = "Адрес не может превышать 500 символов")
        String address
) {}