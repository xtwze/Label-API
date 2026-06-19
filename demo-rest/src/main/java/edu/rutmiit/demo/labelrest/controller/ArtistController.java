package edu.rutmiit.demo.labelrest.controller;

import edu.rutmiit.demo.booksapicontract.dto.Patch.PatchArtistProfileRequest;
import edu.rutmiit.demo.booksapicontract.dto.Patch.PatchArtistRequest;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ArtistProfileResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ArtistResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.PagedResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ReleaseResponse;
import edu.rutmiit.demo.booksapicontract.dto.Update.CreateArtistRequest;
import edu.rutmiit.demo.booksapicontract.dto.Update.UpdateArtistProfileRequest;
import edu.rutmiit.demo.booksapicontract.dto.Update.UpdateArtistRequest;
import edu.rutmiit.demo.booksapicontract.endpoints.ArtistApi;
import edu.rutmiit.demo.labelrest.assembler.ArtistModelAssembler;
import edu.rutmiit.demo.labelrest.assembler.ArtistProfileModelAssembler;
import edu.rutmiit.demo.labelrest.assembler.ReleaseModelAssembler;
import edu.rutmiit.demo.labelrest.service.ArtistService;
import edu.rutmiit.demo.labelrest.service.ReleaseService;
import edu.rutmiit.demo.labelrest.support.PageModelSupport;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Реализация контракта ArtistApi.
 *
 * Аннотации @GetMapping/@PostMapping и т.д. НЕ дублируются здесь — они уже
 * объявлены в интерфейсе ArtistApi, а Spring MVC сканирует аннотации
 * как на классе-реализаторе, так и на реализуемом интерфейсе.
 * Дублирование привело бы к неоднозначному маппингу (или просто избыточному коду).
 */
@RestController
public class ArtistController implements ArtistApi {

    private final ArtistService artistService;
    private final ReleaseService releaseService;
    private final ArtistModelAssembler artistModelAssembler;
    private final ArtistProfileModelAssembler artistProfileModelAssembler;
    private final ReleaseModelAssembler releaseModelAssembler;

    public ArtistController(ArtistService artistService,
                             ReleaseService releaseService,
                             ArtistModelAssembler artistModelAssembler,
                             ArtistProfileModelAssembler artistProfileModelAssembler,
                             ReleaseModelAssembler releaseModelAssembler) {
        this.artistService = artistService;
        this.releaseService = releaseService;
        this.artistModelAssembler = artistModelAssembler;
        this.artistProfileModelAssembler = artistProfileModelAssembler;
        this.releaseModelAssembler = releaseModelAssembler;
    }

    @Override
    public PagedModel<EntityModel<ArtistResponse>> getAllArtists(int page, int size) {
        PagedResponse<ArtistResponse> paged = artistService.findAll(page, size);
        return PageModelSupport.build(paged, artistModelAssembler,
                p -> linkTo(methodOn(ArtistController.class).getAllArtists(p, size)));
    }

    @Override
    public EntityModel<ArtistResponse> getArtistById(Long id) {
        return artistModelAssembler.toModel(artistService.findById(id));
    }

    @Override
    public PagedModel<EntityModel<ArtistResponse>> searchArtists(String name, int page, int size) {
        PagedResponse<ArtistResponse> paged = artistService.search(name, page, size);
        return PageModelSupport.build(paged, artistModelAssembler,
                p -> linkTo(methodOn(ArtistController.class).searchArtists(name, p, size)));
    }

    @Override
    public ResponseEntity<EntityModel<ArtistResponse>> createArtist(CreateArtistRequest request) {
        ArtistResponse created = artistService.create(request);
        EntityModel<ArtistResponse> model = artistModelAssembler.toModel(created);
        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @Override
    public EntityModel<ArtistResponse> updateArtist(Long id, UpdateArtistRequest request) {
        return artistModelAssembler.toModel(artistService.update(id, request));
    }

    @Override
    public EntityModel<ArtistResponse> patchArtist(Long id, PatchArtistRequest request) {
        return artistModelAssembler.toModel(artistService.patch(id, request));
    }

    @Override
    public void deleteArtist(Long id) {
        artistService.delete(id);
    }

    @Override
    public PagedModel<EntityModel<ReleaseResponse>> getReleasesByArtist(Long id, int page, int size) {
        PagedResponse<ReleaseResponse> paged = releaseService.findByArtistId(id, page, size);
        return PageModelSupport.build(paged, releaseModelAssembler,
                p -> linkTo(methodOn(ArtistController.class).getReleasesByArtist(id, p, size)));
    }

    @Override
    public EntityModel<ArtistProfileResponse> getArtistProfile(Long id) {
        return artistProfileModelAssembler.toModel(id, artistService.getProfile(id));
    }

    @Override
    public EntityModel<ArtistProfileResponse> updateArtistProfile(Long id, UpdateArtistProfileRequest request) {
        return artistProfileModelAssembler.toModel(id, artistService.updateProfile(id, request));
    }

    @Override
    public EntityModel<ArtistProfileResponse> patchArtistProfile(Long id, PatchArtistProfileRequest request) {
        return artistProfileModelAssembler.toModel(id, artistService.patchProfile(id, request));
    }
}
