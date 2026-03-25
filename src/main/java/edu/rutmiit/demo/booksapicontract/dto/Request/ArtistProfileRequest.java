package edu.rutmiit.demo.booksapicontract.dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Профиль артиста для договоров")
public record ArtistProfileRequest(

        String firstName, String lastName, String email, String telegramUserId,

        // Паспортные
        @Schema(description = "Дата рождения", example = "1990-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
        @Past LocalDate birthDate,

        @Schema(description = "Серия/номер паспорта", example = "1234 567890", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min=14, max=14) String passport,

        @Schema(description = "Кем выдан", example = "ОВД Москвы", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max=100) String issuedBy,

        @Schema(description = "Дата выдачи", example = "2010-01-01")
        @Past LocalDate issueDate,

        @Schema(description = "Адрес регистрации")
        @NotBlank @Size(max=300) String address,

        @Schema(description = "ИНН", example = "123456789012")
        @Size(min=12, max=12) String inn
) {}