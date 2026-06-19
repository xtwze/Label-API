package edu.rutmiit.demo.labelrest.storage;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Хранилище данных вместо базы. Полный аналог InMemoryStorage из лекции СОП-2,
 * адаптированный под домен лейбла (артисты, профили, релизы).
 *
 * ConcurrentHashMap — потокобезопасная мапа (HTTP-сервер обрабатывает запросы
 * в нескольких потоках одновременно).
 * AtomicLong — потокобезопасный генератор ID (incrementAndGet() атомарен).
 */
@Component
public class InMemoryStorage {

    public final Map<Long, ArtistRecord> artists = new ConcurrentHashMap<>();
    public final Map<Long, ArtistProfileRecord> profiles = new ConcurrentHashMap<>(); // ключ = artistId
    public final Map<Long, ReleaseRecord> releases = new ConcurrentHashMap<>();

    public final AtomicLong artistSequence = new AtomicLong(0);
    public final AtomicLong profileSequence = new AtomicLong(0);
    public final AtomicLong releaseSequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        // --- Артист 1: полностью верифицирован, есть профиль и релизы ---
        ArtistRecord tolstoy = new ArtistRecord();
        tolstoy.setId(artistSequence.incrementAndGet());
        tolstoy.setFirstName("Лев");
        tolstoy.setLastName("Толстой");
        tolstoy.setEmail("tolstoy@label.ru");
        tolstoy.setTelegramUserId("111111111");
        tolstoy.setNickname("tolstoy_lev");
        tolstoy.setVerified(true);
        artists.put(tolstoy.getId(), tolstoy);

        ArtistProfileRecord tolstoyProfile = new ArtistProfileRecord();
        tolstoyProfile.setId(profileSequence.incrementAndGet());
        tolstoyProfile.setArtistId(tolstoy.getId());
        tolstoyProfile.setTelegramUserId(tolstoy.getTelegramUserId());
        tolstoyProfile.setFirstName("Лев");
        tolstoyProfile.setLastName("Толстой");
        tolstoyProfile.setEmail("tolstoy@label.ru");
        tolstoyProfile.setBirthDate(java.time.LocalDate.of(1990, 1, 15));
        tolstoyProfile.setMaskedPassport("1234 ****90");
        tolstoyProfile.setInn("123456789012");
        tolstoyProfile.setAddress("г. Москва, ул. Пушкина, д. 10");
        tolstoyProfile.setVerified(true);
        tolstoyProfile.setUpdatedAt(java.time.Instant.now());
        profiles.put(tolstoy.getId(), tolstoyProfile);

        ReleaseRecord nightDrive = new ReleaseRecord();
        nightDrive.setId(releaseSequence.incrementAndGet());
        nightDrive.setArtistId(tolstoy.getId());
        nightDrive.setTelegramUserId(111111111L);
        nightDrive.setTitle("Night Drive");
        nightDrive.setUpc("123456789012");
        nightDrive.setAlbum(false);
        nightDrive.setDateOfRelease("01.03.2024");
        nightDrive.setGenre("Alternative");
        nightDrive.setDuration("3:21");
        nightDrive.setContractStatus("CREATED");
        nightDrive.setContractUrl("https://api.music-service.ru/contracts/contract_1.pdf");
        nightDrive.setCoverUrl("https://storage.yandexcloud.net/covers/cover_1.jpg");
        nightDrive.setCreatedAt(LocalDateTime.now().minusDays(10));
        nightDrive.setMessage("Договор успешно сформирован");
        releases.put(nightDrive.getId(), nightDrive);

        ReleaseRecord winterAlbum = new ReleaseRecord();
        winterAlbum.setId(releaseSequence.incrementAndGet());
        winterAlbum.setArtistId(tolstoy.getId());
        winterAlbum.setTelegramUserId(111111111L);
        winterAlbum.setTitle("Зимний альбом");
        winterAlbum.setUpc("978543210981");
        winterAlbum.setAlbum(true);
        winterAlbum.setDateOfRelease("15.12.2023");
        winterAlbum.setGenre("Pop");
        winterAlbum.setDuration("38:10");
        winterAlbum.setContractStatus("SIGNED");
        winterAlbum.setContractUrl("https://api.music-service.ru/contracts/contract_2.pdf");
        winterAlbum.setCoverUrl("https://storage.yandexcloud.net/covers/cover_2.jpg");
        winterAlbum.setCreatedAt(LocalDateTime.now().minusMonths(2));
        winterAlbum.setMessage("Договор подписан");
        releases.put(winterAlbum.getId(), winterAlbum);

        tolstoy.setReleasesCount(2);

        // --- Артист 2: без профиля, без релизов (новичок) ---
        ArtistRecord chekhov = new ArtistRecord();
        chekhov.setId(artistSequence.incrementAndGet());
        chekhov.setFirstName("Антон");
        chekhov.setLastName("Чехов");
        chekhov.setEmail("chekhov@label.ru");
        chekhov.setTelegramUserId("222222222");
        chekhov.setNickname("chekhov_anton");
        chekhov.setVerified(false);
        chekhov.setReleasesCount(0);
        artists.put(chekhov.getId(), chekhov);
    }
}
