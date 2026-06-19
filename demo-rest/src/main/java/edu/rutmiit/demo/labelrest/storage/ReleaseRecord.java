package edu.rutmiit.demo.labelrest.storage;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Внутреннее представление релиза.
 *
 * Хранит больше полей, чем отдаёт {@code ReleaseResponse} (genre, duration, dateOfRelease
 * и т.д. в контракте не входят в ответ) — это сделано намеренно, чтобы сервис мог
 * фильтровать/обновлять по этим полям, даже если наружу они пока не отдаются.
 *
 * {@code artistId} — связь с артистом, вычисляется в ReleaseService при создании
 * релиза по совпадению telegramUserId с зарегистрированным артистом.
 */
@Data
public class ReleaseRecord {
    private Long id;
    private Long artistId;
    private Long telegramUserId;
    private String title;
    private String upc;
    private boolean album;
    private String dateOfRelease;
    private String yearOfCreation;
    private String genre;
    private String duration;
    private String artistShares;
    private String lyricistNames;
    private String cover;
    private String beatmakersNames;
    private String contractStatus;
    private String contractUrl;
    private String coverUrl;
    private LocalDateTime createdAt;
    private String message;
}
