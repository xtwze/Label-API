package edu.rutmiit.demo.labelrest.storage;

import lombok.Data;

/**
 * Внутреннее представление артиста в хранилище.
 *
 * Это НЕ DTO контракта — ArtistResponse собирается из этого объекта в сервисе.
 * Разделение нужно, потому что DTO контракта (immutable, @Builder) неудобно
 * мутировать при PATCH/PUT, а внутреннее хранилище должно легко обновляться.
 */
@Data
public class ArtistRecord {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String telegramUserId;
    private String nickname;
    private boolean verified;
    private int releasesCount;
}
