package edu.rutmiit.demo.booksapicontract.dto.Responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "profiles", itemRelation = "profile")
@Schema(description = "Информация о профиле артиста")
public class ArtistProfileResponse extends RepresentationModel<ArtistProfileResponse> {

    @Schema(description = "Уникальный ID профиля в системе", example = "501")
    private final Long id;

    @Schema(description = "ID пользователя в Telegram", example = "12345678")
    private final String telegramUserId;

    @Schema(description = "Имя", example = "Иван")
    private final String firstName;

    @Schema(description = "Фамилия", example = "Иванов")
    private final String lastName;

    @Schema(description = "Контактный Email", example = "artist@example.com")
    private final String email;

    @Schema(description = "Дата рождения", example = "1990-01-01")
    private final LocalDate birthDate;

    @Schema(description = "Маскированный номер паспорта для безопасности", example = "1234 ****90")
    private final String maskedPassport;

    @Schema(description = "ИНН", example = "123456789012")
    private final String inn;

    @Schema(description = "Адрес регистрации", example = "г. Москва, ул. Пушкина, д. 10")
    private final String address;

    @Schema(description = "Статус верификации данных (заполнены ли все поля для договора)")
    private final boolean isVerified;

    @Schema(description = "Дата последнего обновления профиля")
    private final Instant updatedAt;
}