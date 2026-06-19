package edu.rutmiit.demo.booksapicontract.dto.Request;

import edu.rutmiit.demo.booksapicontract.validation.ValidUpc;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Данные о релизе для формирования договора")
public record ReleaseRequest(
        @Schema(description = "ID пользователя в Telegram", example = "12345678")
        @NotNull Long telegramUserId,

        @Schema(description = "Название релиза", example = "Название релиза")
        @NotBlank String title,

        @Schema(description = "Уникальный штрих-код релиза (UPC-A, 12 цифр)", example = "123456789012")
        @ValidUpc
        String upc,

        @Schema(description = "Является ли альбомом")
        boolean isAlbum,

        @Schema(description = "Дата релиза", example = "ДД.ММ.ГГГГ")
        String dateOfRelease,
        @Schema(description = "Год создания(при переносе релиза)", example = "2005")
        String yearOfCreation,

        @Schema(description = "Жанр", example = "Alternative")
        String genre,
        @Schema(description = "Продолжительность", example = "1:23")
        String duration,

        @Schema(description = "Список артистов и их доли (JSON-строка или структура)")
        String artistShares,



        @Schema(description = "Имена авторов текста", example = "Иван Иванов")
        String lyricistNames,

        //todo спросить как из сервера a перенести не json в сервер b
        @Schema(description = "Обложка", example = "Представим обложку")
        String cover,
        @Schema(description = "Имена авторов инструментала", example = "Иван Иванов")
        String beatmakersNames

) {}