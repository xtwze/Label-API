package edu.rutmiit.demo.labelrest.service;

import edu.rutmiit.demo.booksapicontract.dto.Patch.PatchArtistProfileRequest;
import edu.rutmiit.demo.booksapicontract.dto.Patch.PatchArtistRequest;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ArtistProfileResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ArtistResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.PagedResponse;
import edu.rutmiit.demo.booksapicontract.dto.Update.CreateArtistRequest;
import edu.rutmiit.demo.booksapicontract.dto.Update.UpdateArtistProfileRequest;
import edu.rutmiit.demo.booksapicontract.dto.Update.UpdateArtistRequest;
import edu.rutmiit.demo.booksapicontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.labelrest.storage.ArtistProfileRecord;
import edu.rutmiit.demo.labelrest.storage.ArtistRecord;
import edu.rutmiit.demo.labelrest.storage.InMemoryStorage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class ArtistService {

    private final InMemoryStorage storage;
    private final ReleaseService releaseService;

    /**
     * ArtistService <-> ReleaseService — циклическая зависимость (артист каскадно
     * удаляет релизы, релиз при создании ищет артиста по telegramUserId).
     * @Lazy на одном из параметров разрывает цикл: Spring создаёт CGLIB-прокси
     * вместо реального бина, а сам бин — только при первом реальном вызове.
     */
    public ArtistService(InMemoryStorage storage, @Lazy ReleaseService releaseService) {
        this.storage = storage;
        this.releaseService = releaseService;
    }

    public PagedResponse<ArtistResponse> findAll(int page, int size) {
        List<ArtistRecord> all = storage.artists.values().stream()
                .sorted(Comparator.comparingLong(ArtistRecord::getId))
                .toList();
        return paginate(all, page, size);
    }

    /** Задание 3: поиск по подстроке в firstName+lastName, без учёта регистра. */
    public PagedResponse<ArtistResponse> search(String name, int page, int size) {
        String needle = name.toLowerCase(Locale.ROOT);
        List<ArtistRecord> filtered = storage.artists.values().stream()
                .filter(a -> (a.getFirstName() + " " + a.getLastName())
                        .toLowerCase(Locale.ROOT)
                        .contains(needle))
                .sorted(Comparator.comparingLong(ArtistRecord::getId))
                .toList();
        return paginate(filtered, page, size);
    }

    private PagedResponse<ArtistResponse> paginate(List<ArtistRecord> records, int page, int size) {
        int totalElements = records.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<ArtistResponse> content = (from >= totalElements)
                ? List.of()
                : records.subList(from, to).stream().map(this::toResponse).toList();
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public ArtistRecord findRecordById(Long id) {
        ArtistRecord record = storage.artists.get(id);
        if (record == null) {
            throw new ResourceNotFoundException("Артист", id);
        }
        return record;
    }

    public ArtistResponse findById(Long id) {
        return toResponse(findRecordById(id));
    }

    public ArtistResponse create(CreateArtistRequest request) {
        long id = storage.artistSequence.incrementAndGet();
        ArtistRecord record = new ArtistRecord();
        record.setId(id);
        record.setFirstName(request.firstName());
        record.setLastName(request.lastName());
        record.setEmail(request.email());
        record.setNickname(request.nickname());
        record.setTelegramUserId(request.telegramUserId());
        record.setVerified(false);
        record.setReleasesCount(0);
        storage.artists.put(id, record);
        return toResponse(record);
    }

    /** PUT — полная замена данных артиста (все поля контракта обязательны). */
    public ArtistResponse update(Long id, UpdateArtistRequest request) {
        ArtistRecord existing = findRecordById(id);
        existing.setFirstName(request.firstName());
        existing.setLastName(request.lastName());
        existing.setEmail(request.email());
        existing.setNickname(request.nickname());
        existing.setTelegramUserId(request.telegramUserId());
        return toResponse(existing);
    }

    /** PATCH — частичное обновление, null = «не менять». */
    public ArtistResponse patch(Long id, PatchArtistRequest request) {
        ArtistRecord existing = findRecordById(id);
        if (request.firstName() != null) existing.setFirstName(request.firstName());
        if (request.lastName() != null) existing.setLastName(request.lastName());
        if (request.email() != null) existing.setEmail(request.email());
        if (request.nickname() != null) existing.setNickname(request.nickname());
        return toResponse(existing);
    }

    /** Каскадное удаление: сначала все релизы и профиль артиста, затем сам артист. */
    public void delete(Long id) {
        findRecordById(id);
        releaseService.deleteByArtistId(id);
        storage.profiles.remove(id);
        storage.artists.remove(id);
    }

    public ArtistProfileResponse getProfile(Long id) {
        findRecordById(id);
        ArtistProfileRecord profile = storage.profiles.get(id);
        if (profile == null) {
            throw new ResourceNotFoundException("Профиль артиста", id);
        }
        return toProfileResponse(profile);
    }

    /**
     * PUT профиля — создаёт профиль, если его ещё нет, либо полностью заменяет существующий.
     * maskedPassport приходит от клиента уже в замаскированном виде (контракт это требует).
     */
    public ArtistProfileResponse updateProfile(Long id, UpdateArtistProfileRequest request) {
        ArtistRecord artist = findRecordById(id);
        ArtistProfileRecord profile = getOrCreateProfile(id, artist);

        profile.setFirstName(request.firstName());
        profile.setLastName(request.lastName());
        profile.setEmail(request.email());
        profile.setBirthDate(request.birthDate());
        profile.setMaskedPassport(request.maskedPassport());
        profile.setInn(request.inn());
        profile.setAddress(request.address());
        profile.setVerified(true);
        profile.setUpdatedAt(Instant.now());
        return toProfileResponse(profile);
    }

    /** PATCH профиля — null-поля не трогаем; сырой паспорт маскируем перед сохранением. */
    public ArtistProfileResponse patchProfile(Long id, PatchArtistProfileRequest request) {
        ArtistRecord artist = findRecordById(id);
        ArtistProfileRecord profile = getOrCreateProfile(id, artist);

        if (request.firstName() != null) profile.setFirstName(request.firstName());
        if (request.lastName() != null) profile.setLastName(request.lastName());
        if (request.email() != null) profile.setEmail(request.email());
        if (request.telegramUserId() != null) profile.setTelegramUserId(request.telegramUserId());
        if (request.birthDate() != null) profile.setBirthDate(request.birthDate());
        if (request.passport() != null) profile.setMaskedPassport(maskPassport(request.passport()));
        if (request.inn() != null) profile.setInn(request.inn());
        if (request.address() != null) profile.setAddress(request.address());

        profile.setVerified(isProfileComplete(profile));
        profile.setUpdatedAt(Instant.now());
        return toProfileResponse(profile);
    }

    private ArtistProfileRecord getOrCreateProfile(Long artistId, ArtistRecord artist) {
        return storage.profiles.computeIfAbsent(artistId, k -> {
            ArtistProfileRecord p = new ArtistProfileRecord();
            p.setId(storage.profileSequence.incrementAndGet());
            p.setArtistId(artistId);
            p.setTelegramUserId(artist.getTelegramUserId());
            return p;
        });
    }

    private boolean isProfileComplete(ArtistProfileRecord p) {
        return p.getFirstName() != null && p.getLastName() != null && p.getEmail() != null
                && p.getBirthDate() != null && p.getMaskedPassport() != null
                && p.getInn() != null && p.getAddress() != null;
    }

    /**
     * Маскирует «сырой» номер паспорта: оставляет первые 4 и последние 2 цифры видимыми,
     * остальное заменяет звёздочками. Пример: "1234567890" -> "1234 ****90".
     */
    private String maskPassport(String raw) {
        String digits = raw.replaceAll("\\D", "");
        if (digits.length() < 6) {
            return raw;
        }
        String first = digits.substring(0, 4);
        String last = digits.substring(digits.length() - 2);
        String stars = "*".repeat(digits.length() - 6);
        return first + " " + stars + last;
    }

    /** Задание 4: динамический пересчёт количества релизов артиста. */
    public void recalculateReleasesCount(Long artistId) {
        ArtistRecord record = storage.artists.get(artistId);
        if (record == null) {
            return;
        }
        long count = storage.releases.values().stream()
                .filter(r -> artistId.equals(r.getArtistId()))
                .count();
        record.setReleasesCount((int) count);
    }

    ArtistResponse toResponse(ArtistRecord r) {
        return ArtistResponse.builder()
                .id(r.getId())
                .firstName(r.getFirstName())
                .lastName(r.getLastName())
                .fullName(r.getFirstName() + " " + r.getLastName())
                .email(r.getEmail())
                .telegramUserId(r.getTelegramUserId())
                .nickname(r.getNickname())
                .isVerified(r.isVerified())
                .releasesCount(r.getReleasesCount())
                .build();
    }

    private ArtistProfileResponse toProfileResponse(ArtistProfileRecord p) {
        return ArtistProfileResponse.builder()
                .id(p.getId())
                .telegramUserId(p.getTelegramUserId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .birthDate(p.getBirthDate())
                .maskedPassport(p.getMaskedPassport())
                .inn(p.getInn())
                .address(p.getAddress())
                .isVerified(p.isVerified())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
