package edu.rutmiit.demo.booksapicontract.dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Запрос на создание или обновление артиста")
public record ArtistRequest(
        @Schema(description = "Имя артиста", example = "Михаил", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя не может быть пустым")
        @Size(max = 100, message = "Имя ≤ 100 символов")
        String firstName,

        @Schema(description = "Фамилия артиста", example = "Константинов", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Фамилия не может быть пустой")
        @Size(max = 100, message = "Фамилия ≤ 100 символов")
        String lastName,

        @Schema(description = "Email артиста", example = "konstantinov@example.com")
        @Email(message = "Некорректный email")
        @Size(max = 255, message = "Email ≤ 255 символов")
        String email,

        @Schema(description = "Никнейм артиста", example = "xtwze")
        @Size(max = 55, message = "Никнейм артиста не может быть больше 55 символов")
        String nickname,

        @Schema(description = "Telegram ID артиста", example = "123456789")
        @Size(max = 20, message = "Telegram ID ≤ 20 символов")
        String telegramUserId
) {}