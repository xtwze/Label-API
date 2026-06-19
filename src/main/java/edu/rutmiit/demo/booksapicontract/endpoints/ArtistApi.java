package edu.rutmiit.demo.booksapicontract.endpoints;

import edu.rutmiit.demo.booksapicontract.config.LabelApiContractConfig;
import edu.rutmiit.demo.booksapicontract.dto.Patch.PatchArtistProfileRequest;
import edu.rutmiit.demo.booksapicontract.dto.Patch.PatchArtistRequest;
import edu.rutmiit.demo.booksapicontract.dto.Update.CreateArtistRequest;
import edu.rutmiit.demo.booksapicontract.dto.Update.UpdateArtistProfileRequest;
import edu.rutmiit.demo.booksapicontract.dto.Update.UpdateArtistRequest;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ArtistProfileResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ArtistResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ErrorResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ReleaseResponse;
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
 * Контракт API для управления артистами лейбла.
 */
@Tag(name = "Artists", description = "Управление артистами и их творческими профилями")
@RequestMapping(
        value = "/api/artists",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface ArtistApi {

    @Operation(
            summary = "Получить список артистов",
            description = "Возвращает постраничный список всех зарегистрированных артистов лейбла.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список артистов успешно получен")
    @GetMapping
    PagedModel<EntityModel<ArtistResponse>> getAllArtists(
            @Parameter(description = "Номер страницы (0..N)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20")
            @RequestParam(defaultValue = "20") int size
    );

    @Operation(
            summary = "Найти артиста по ID",
            description = "Возвращает полные данные артиста, включая статус верификации и никнейм.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Артист найден")
    @ApiResponse(responseCode = "404", description = "Артист с таким ID не зарегистрирован",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<ArtistResponse> getArtistById(
            @Parameter(description = "Внутренний ID артиста", required = true, example = "1")
            @PathVariable Long id
    );

    @Operation(
            summary = "Регистрация нового артиста",
            description = "Создает базовую запись артиста. Персональные данные (паспорт) заполняются отдельно через профиль артиста.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Артист успешно создан")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации данных (некорректный email или никнейм)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<ArtistResponse>> createArtist(@Valid @RequestBody CreateArtistRequest request);

    @Operation(
            summary = "Полное обновление данных артиста (PUT)",
            description = "Требует передачи всех обязательных полей UpdateArtistRequest. Используется для полной перезаписи данных.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Данные успешно обновлены")
    @ApiResponse(responseCode = "404", description = "Артист не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<ArtistResponse> updateArtist(
            @Parameter(description = "ID артиста", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateArtistRequest request
    );

    @Operation(
            summary = "Частичное обновление артиста (PATCH)",
            description = "Позволяет изменить только отдельные поля (например, только никнейм или email).",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Поля успешно обновлены")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<ArtistResponse> patchArtist(
            @Parameter(description = "ID артиста", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody PatchArtistRequest request
    );

    @Operation(
            summary = "Удалить артиста из системы",
            description = "Удаляет запись об артисте. Внимание: может повлечь каскадное удаление связанных релизов.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Артист успешно удален")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteArtist(
            @Parameter(description = "ID артиста", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Список релизов артиста (дискография)",
            description = "Суб-ресурс, возвращающий все музыкальные релизы (синглы/альбомы) данного артиста.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список релизов получен")
    @ApiResponse(responseCode = "404", description = "Артист не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}/releases")
    PagedModel<EntityModel<ReleaseResponse>> getReleasesByArtist(
            @Parameter(description = "ID артиста", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Номер страницы", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );

    @Operation(
            summary = "Получить профиль артиста (паспортные данные)",
            description = "Возвращает данные, необходимые для генерации договора: дата рождения, паспорт, ИНН, адрес. " +
                    "Паспортный номер возвращается в замаскированном виде.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Профиль найден")
    @ApiResponse(responseCode = "404", description = "Артист или его профиль не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}/profile")
    EntityModel<ArtistProfileResponse> getArtistProfile(
            @Parameter(description = "ID артиста", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Полное обновление профиля артиста (PUT)",
            description = "Требует передачи всех обязательных полей UpdateArtistProfileRequest для формирования договора.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Профиль успешно обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации паспортных данных",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Артист не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<ArtistProfileResponse> updateArtistProfile(
            @Parameter(description = "ID артиста", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateArtistProfileRequest request
    );

    @Operation(
            summary = "Частичное обновление профиля артиста (PATCH)",
            description = "Позволяет изменить только отдельные паспортные поля, не затрагивая остальные.",
            security = @SecurityRequirement(name = LabelApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Поля профиля успешно обновлены")
    @ApiResponse(responseCode = "404", description = "Артист не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<ArtistProfileResponse> patchArtistProfile(
            @Parameter(description = "ID артиста", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody PatchArtistProfileRequest request
    );
}