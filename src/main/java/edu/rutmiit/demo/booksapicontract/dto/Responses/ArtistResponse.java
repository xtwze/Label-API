package edu.rutmiit.demo.booksapicontract.dto.Responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "artists", itemRelation = "artist")
@Schema(description = "Информация об артисте (ответ сервера)")
public class ArtistResponse extends RepresentationModel<ArtistResponse> {

    @Schema(description = "Уникальный ID в системе веба", example = "1")
    private final Long id;

    @Schema(description = "Имя артиста", example = "Михаил")
    private final String firstName;

    @Schema(description = "Фамилия артиста", example = "Константинов")
    private final String lastName;

    @Schema(description = "Полное имя для отображения", example = "Михаил Константинов")
    private final String fullName;

    @Schema(description = "Email для связи", example = "konstantinov@gmail.com")
    private final String email;

    @Schema(description = "ID пользователя в Telegram", example = "123456789")
    private final String telegramUserId;

    @Schema(description = "Никнейм артиста", example = "xtwze")
    private final Integer nickname;

    @Schema(description = "Флаг готовности документов (пройдена ли проверка данных)", example = "true")
    private final Boolean isVerified;

}