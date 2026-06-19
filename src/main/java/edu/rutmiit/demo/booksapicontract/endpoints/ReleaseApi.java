package edu.rutmiit.demo.booksapicontract.endpoints;

import edu.rutmiit.demo.booksapicontract.config.LabelApiContractConfig;
import edu.rutmiit.demo.booksapicontract.dto.Patch.PatchReleaseRequest;
import edu.rutmiit.demo.booksapicontract.dto.Request.ReleaseRequest;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ReleaseResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контракт API для управления музыкальными релизами.
 */
@Tag(name = "Releases", description = "Управление музыкальными релизами (синглы, альбомы)")
@RequestMapping(
        value = "/api/releases",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface ReleaseApi {

    @Operation(
            summary = "Зарегистрировать новый релиз",
            description = "Создает запись о релизе для последующей генерации договора. UPC должен быть уникальным.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Релиз успешно создан")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации данных релиза (например, некорректный UPC)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Релиз с таким UPC уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<ReleaseResponse>> createRelease(@Valid @RequestBody ReleaseRequest request);

    @Operation(
            summary = "Получить релиз по ID",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Релиз найден")
    @ApiResponse(responseCode = "404", description = "Релиз не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<ReleaseResponse> getReleaseById(
            @Parameter(description = "ID релиза", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Список всех релизов",
            description = "Возвращает постраничный список релизов с фильтрацией по жанру, артисту или названию.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Постраничный список релизов")
    @GetMapping
    PagedModel<EntityModel<ReleaseResponse>> getAllReleases(
            @Parameter(description = "ID артиста") @RequestParam(required = false) Long artistId,
            @Parameter(description = "Жанр", example = "Alternative") @RequestParam(required = false) String genre,
            @Parameter(description = "Поиск по названию", example = "Night") @RequestParam(required = false) String titleSearch,
            @Parameter(description = "Номер страницы", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );

    @Operation(
            summary = "Обновить статус или данные релиза (PATCH)",
            description = "Частичное обновление метаданных релиза.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Релиз обновлен")
    @ApiResponse(responseCode = "404", description = "Релиз не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<ReleaseResponse> patchRelease(
            @Parameter(description = "ID релиза", required = true) @PathVariable Long id,
            @Valid @RequestBody PatchReleaseRequest request
    );

    @Operation(
            summary = "Удалить релиз",
            description = "Удаляет релиз и связанные файлы. Внимание: действие необратимо.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Релиз удален")
    @ApiResponse(responseCode = "404", description = "Релиз не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRelease(
            @Parameter(description = "ID релиза", required = true) @PathVariable Long id
    );
}