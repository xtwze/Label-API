package edu.rutmiit.demo.booksapicontract.dto.Responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

/**
 * Данные о созданном релизе в ответе API.
 * Расширяет RepresentationModel для поддержки HATEOAS-ссылок (на договор, на обложку и т.д.).
 */
@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "releases", itemRelation = "release")
@Schema(description = "Информация о созданном релизе и статусе договора")
public class ReleaseResponse extends RepresentationModel<ReleaseResponse> {

    @Schema(description = "Уникальный идентификатор релиза в системе", example = "101")
    private final Long id;

    @Schema(description = "ID пользователя в Telegram", example = "12345678")
    private final Long telegramUserId;

    @Schema(description = "Название релиза", example = "Night Drive")
    private final String title;

    @Schema(description = "Статус генерации договора", example = "CREATED",
            allowableValues = {"PENDING", "CREATED", "SIGNED", "ERROR"})
    private final String contractStatus;

    @Schema(description = "URL для скачивания сгенерированного договора",
            example = "https://api.music-service.ru/contracts/contract_101.pdf")
    private final String contractUrl;

    @Schema(description = "URL загруженной обложки",
            example = "https://storage.yandexcloud.net/covers/cover_101.jpg")
    private final String coverUrl;

    @Schema(description = "Дата и время регистрации релиза в системе")
    private final LocalDateTime createdAt;

    @Schema(description = "Дополнительное сообщение об состоянии договора (например, причина ошибки)", example = "Договор успешно сформирован")
    private final String message;
}