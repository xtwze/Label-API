package edu.rutmiit.demo.labelrest.controller;

import edu.rutmiit.demo.booksapicontract.dto.Patch.PatchReleaseRequest;
import edu.rutmiit.demo.booksapicontract.dto.Request.ReleaseRequest;
import edu.rutmiit.demo.booksapicontract.dto.Responses.PagedResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ReleaseResponse;
import edu.rutmiit.demo.booksapicontract.dto.Responses.ReleaseSummaryResponse;
import edu.rutmiit.demo.booksapicontract.endpoints.ReleaseApi;
import edu.rutmiit.demo.labelrest.assembler.ReleaseModelAssembler;
import edu.rutmiit.demo.labelrest.assembler.ReleaseSummaryModelAssembler;
import edu.rutmiit.demo.labelrest.service.ReleaseService;
import edu.rutmiit.demo.labelrest.support.PageModelSupport;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ReleaseController implements ReleaseApi {

    private final ReleaseService releaseService;
    private final ReleaseModelAssembler releaseModelAssembler;
    private final ReleaseSummaryModelAssembler releaseSummaryModelAssembler;

    public ReleaseController(ReleaseService releaseService,
                              ReleaseModelAssembler releaseModelAssembler,
                              ReleaseSummaryModelAssembler releaseSummaryModelAssembler) {
        this.releaseService = releaseService;
        this.releaseModelAssembler = releaseModelAssembler;
        this.releaseSummaryModelAssembler = releaseSummaryModelAssembler;
    }

    @Override
    public ResponseEntity<EntityModel<ReleaseResponse>> createRelease(ReleaseRequest request) {
        ReleaseResponse created = releaseService.create(request);
        EntityModel<ReleaseResponse> model = releaseModelAssembler.toModel(created);
        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @Override
    public EntityModel<ReleaseResponse> getReleaseById(Long id) {
        return releaseModelAssembler.toModel(releaseService.findById(id));
    }

    @Override
    public PagedModel<EntityModel<ReleaseResponse>> getAllReleases(Long artistId, String genre, String titleSearch, int page, int size) {
        PagedResponse<ReleaseResponse> paged = releaseService.findAll(artistId, genre, titleSearch, page, size);
        return PageModelSupport.build(paged, releaseModelAssembler,
                p -> linkTo(methodOn(ReleaseController.class).getAllReleases(artistId, genre, titleSearch, p, size)));
    }

    @Override
    public PagedModel<EntityModel<ReleaseSummaryResponse>> getAllReleasesSummary(int page, int size) {
        PagedResponse<ReleaseSummaryResponse> paged = releaseService.findAllSummary(page, size);
        return PageModelSupport.build(paged, releaseSummaryModelAssembler,
                p -> linkTo(methodOn(ReleaseController.class).getAllReleasesSummary(p, size)));
    }

    @Override
    public EntityModel<ReleaseResponse> patchRelease(Long id, PatchReleaseRequest request) {
        return releaseModelAssembler.toModel(releaseService.patch(id, request));
    }

    @Override
    public void deleteRelease(Long id) {
        releaseService.delete(id);
    }
}
