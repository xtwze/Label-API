package edu.rutmiit.demo.labelrest.service;

import edu.rutmiit.demo.booksapicontract.dto.Patch.PatchReleaseRequest;
import edu.rutmiit.demo.booksapicontract.dto.Request.ReleaseRequest;
import edu.rutmiit.demo.booksapicontract.dto.Responses.PagedResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ReleaseResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ReleaseSummaryResponse;
import edu.rutmiit.demo.booksapicontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.booksapicontract.exception.UpcAlreadyExistsException;
import edu.rutmiit.demo.labelrest.storage.ArtistRecord;
import edu.rutmiit.demo.labelrest.storage.InMemoryStorage;
import edu.rutmiit.demo.labelrest.storage.ReleaseRecord;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class ReleaseService {

    private final InMemoryStorage storage;
    private final ArtistService artistService;

    public ReleaseService(InMemoryStorage storage, @Lazy ArtistService artistService) {
        this.storage = storage;
        this.artistService = artistService;
    }

    /**
     * Создаёт релиз. Артист ищется по telegramUserId (так релиз приходит от бота
     * ещё до того, как нам известен внутренний веб-ID артиста). Если совпадение
     * не найдено — релиз всё равно создаётся, но без привязки к артисту
     * (artistId = null); это нормальная ситуация в реальном потоке бота.
     */
    public ReleaseResponse create(ReleaseRequest request) {
        validateUpc(request.upc(), null);
        ArtistRecord artist = findArtistByTelegramId(request.telegramUserId());

        long id = storage.releaseSequence.incrementAndGet();
        ReleaseRecord record = new ReleaseRecord();
        record.setId(id);
        record.setArtistId(artist != null ? artist.getId() : null);
        record.setTelegramUserId(request.telegramUserId());
        record.setTitle(request.title());
        record.setUpc(request.upc());
        record.setAlbum(request.isAlbum());
        record.setDateOfRelease(request.dateOfRelease());
        record.setYearOfCreation(request.yearOfCreation());
        record.setGenre(request.genre());
        record.setDuration(request.duration());
        record.setArtistShares(request.artistShares());
        record.setLyricistNames(request.lyricistNames());
        record.setCover(request.cover());
        record.setBeatmakersNames(request.beatmakersNames());
        record.setContractStatus("PENDING");
        record.setContractUrl(null);
        record.setCoverUrl(request.cover());
        record.setCreatedAt(LocalDateTime.now());
        record.setMessage("Договор формируется");

        storage.releases.put(id, record);
        if (artist != null) {
            artistService.recalculateReleasesCount(artist.getId());
        }
        return toResponse(record);
    }

    private ArtistRecord findArtistByTelegramId(Long telegramUserId) {
        if (telegramUserId == null) {
            return null;
        }
        String needle = String.valueOf(telegramUserId);
        return storage.artists.values().stream()
                .filter(a -> needle.equals(a.getTelegramUserId()))
                .findFirst()
                .orElse(null);
    }

    public ReleaseResponse findById(Long id) {
        return toResponse(findRecordById(id));
    }

    ReleaseRecord findRecordById(Long id) {
        ReleaseRecord record = storage.releases.get(id);
        if (record == null) {
            throw new ResourceNotFoundException("Релиз", id);
        }
        return record;
    }

    public PagedResponse<ReleaseResponse> findAll(Long artistId, String genre, String titleSearch, int page, int size) {
        List<ReleaseRecord> filtered = storage.releases.values().stream()
                .filter(r -> artistId == null || artistId.equals(r.getArtistId()))
                .filter(r -> genre == null || genre.equalsIgnoreCase(r.getGenre()))
                .filter(r -> titleSearch == null || (r.getTitle() != null
                        && r.getTitle().toLowerCase(Locale.ROOT).contains(titleSearch.toLowerCase(Locale.ROOT))))
                .sorted(Comparator.comparingLong(ReleaseRecord::getId))
                .toList();
        return paginate(filtered, page, size);
    }

    /** Суб-ресурс GET /api/artists/{id}/releases — сначала убеждаемся, что артист существует. */
    public PagedResponse<ReleaseResponse> findByArtistId(Long artistId, int page, int size) {
        artistService.findRecordById(artistId);
        return findAll(artistId, null, null, page, size);
    }

    /** Задание 5: облегчённый каталог релизов. */
    public PagedResponse<ReleaseSummaryResponse> findAllSummary(int page, int size) {
        List<ReleaseRecord> all = storage.releases.values().stream()
                .sorted(Comparator.comparingLong(ReleaseRecord::getId))
                .toList();

        int totalElements = all.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<ReleaseSummaryResponse> content = (from >= totalElements)
                ? List.of()
                : all.subList(from, to).stream().map(this::toSummary).toList();
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    private PagedResponse<ReleaseResponse> paginate(List<ReleaseRecord> records, int page, int size) {
        int totalElements = records.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<ReleaseResponse> content = (from >= totalElements)
                ? List.of()
                : records.subList(from, to).stream().map(this::toResponse).toList();
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public ReleaseResponse patch(Long id, PatchReleaseRequest request) {
        ReleaseRecord existing = findRecordById(id);
        if (request.upc() != null) {
            validateUpc(request.upc(), id);
            existing.setUpc(request.upc());
        }
        if (request.title() != null) existing.setTitle(request.title());
        if (request.dateOfRelease() != null) existing.setDateOfRelease(request.dateOfRelease());
        if (request.genre() != null) existing.setGenre(request.genre());
        if (request.duration() != null) existing.setDuration(request.duration());
        if (request.cover() != null) {
            existing.setCover(request.cover());
            existing.setCoverUrl(request.cover());
        }
        return toResponse(existing);
    }

    public void delete(Long id) {
        ReleaseRecord record = findRecordById(id);
        storage.releases.remove(id);
        if (record.getArtistId() != null) {
            artistService.recalculateReleasesCount(record.getArtistId());
        }
    }

    /** Используется при каскадном удалении артиста. */
    public void deleteByArtistId(Long artistId) {
        List<Long> ids = storage.releases.values().stream()
                .filter(r -> artistId.equals(r.getArtistId()))
                .map(ReleaseRecord::getId)
                .toList();
        ids.forEach(storage.releases::remove);
    }

    private void validateUpc(String upc, Long currentReleaseId) {
        if (upc == null) {
            return;
        }
        storage.releases.values().stream()
                .filter(r -> upc.equalsIgnoreCase(r.getUpc()))
                .filter(r -> !r.getId().equals(currentReleaseId))
                .findAny()
                .ifPresent(r -> { throw new UpcAlreadyExistsException(upc); });
    }

    ReleaseResponse toResponse(ReleaseRecord r) {
        return ReleaseResponse.builder()
                .id(r.getId())
                .telegramUserId(r.getTelegramUserId())
                .title(r.getTitle())
                .upc(r.getUpc())
                .contractStatus(r.getContractStatus())
                .contractUrl(r.getContractUrl())
                .coverUrl(r.getCoverUrl())
                .createdAt(r.getCreatedAt())
                .message(r.getMessage())
                .build();
    }

    private ReleaseSummaryResponse toSummary(ReleaseRecord r) {
        return ReleaseSummaryResponse.builder()
                .id(r.getId())
                .title(r.getTitle())
                .upc(r.getUpc())
                .contractStatus(r.getContractStatus())
                .build();
    }
}
