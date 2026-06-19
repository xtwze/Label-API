package edu.rutmiit.demo.labelrest.storage;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Внутреннее представление профиля артиста (паспортные данные для договора).
 * Ключ в хранилище — artistId (один профиль на одного артиста).
 */
@Data
public class ArtistProfileRecord {
    private Long id;
    private Long artistId;
    private String telegramUserId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;

    /**
     * Хранится УЖЕ в замаскированном виде (например, "1234 ****90").
     * Маскирование выполняется в сервисе при PATCH (из сырого паспорта),
     * а при PUT клиент обязан передать уже замаскированное значение (см. UpdateArtistProfileRequest).
     */
    private String maskedPassport;
    private String inn;
    private String address;
    private boolean verified;
    private Instant updatedAt;
}
