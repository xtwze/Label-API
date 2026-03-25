package edu.rutmiit.demo.booksapicontract.dto.Patch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Запрос для частичного обновления книги (PATCH, семантика JSON Merge Patch).
 *
 * Передайте только те поля, которые нужно изменить.
 * Поля, отсутствующие в запросе, десериализуются как null — сервис их не трогает.
 *
 * Ограничение: стандартный Jackson не различает «поле не пришло» и «пришло явно null».
 * В этом контракте оба случая означают «не менять». Для точного различения
 * можно использовать JsonNullable из библиотеки jackson-databind-nullable.
 *
 * Сменить автора через PATCH нельзя — для этого создайте новую книгу.
 */

import java.time.LocalDate;

@Schema(description = "Частичное обновление профиля артиста. Передайте только те поля, которые нужно изменить.")
public record PatchArtistProfileRequest(

        @Schema(description = "Имя", example = "Михаил")
        @Size(max = 100) String firstName,

        @Schema(description = "Фамилия", example = "Константинов")
        @Size(max = 100) String lastName,

        @Schema(description = "Email", example = "misha@music.ru")
        @Email String email,

        @Schema(description = "Telegram ID", example = "123456789")
        @Size(max = 20) String telegramUserId,

        @Schema(description = "Дата рождения", example = "1990-01-01")
        @Past LocalDate birthDate,

        @Schema(description = "Серия и номер паспорта", example = "1234 567890")
        @Size(min = 14, max = 14) String passport,

        @Schema(description = "Кем выдан паспорт", example = "ГУ МВД по г. Москве")
        @Size(max = 100) String issuedBy,

        @Schema(description = "Дата выдачи паспорта", example = "2010-01-01")
        @Past LocalDate issueDate,

        @Schema(description = "Адрес регистрации (прописка)", example = "г. Москва, ул. Образцова, д. 9 с 1")
        @Size(max = 300) String address,

        @Schema(description = "ИНН (12 цифр)", example = "123456789012")
        @Size(min = 12, max = 12) String inn
) {}
