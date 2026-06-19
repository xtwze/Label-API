package edu.rutmiit.demo.labelrest.assembler;

import edu.rutmiit.demo.booksapicontract.dto.Responses.ReleaseResponse;
import edu.rutmiit.demo.labelrest.controller.ArtistController;
import edu.rutmiit.demo.labelrest.controller.ReleaseController;
import edu.rutmiit.demo.labelrest.storage.InMemoryStorage;
import edu.rutmiit.demo.labelrest.storage.ReleaseRecord;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Собирает EntityModel&lt;ReleaseResponse&gt;.
 *
 * ReleaseResponse не содержит artistId (так устроен контракт), поэтому, чтобы
 * добавить ссылку на артиста-владельца релиза, ассемблер обращается к хранилищу
 * напрямую — это нормальная практика для assembler-слоя, который как раз и
 * отвечает за обогащение ответа дополнительной навигацией.
 */
@Component
public class ReleaseModelAssembler implements RepresentationModelAssembler<ReleaseResponse, EntityModel<ReleaseResponse>> {

    private final InMemoryStorage storage;

    public ReleaseModelAssembler(InMemoryStorage storage) {
        this.storage = storage;
    }

    @Override
    public EntityModel<ReleaseResponse> toModel(ReleaseResponse release) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(ReleaseController.class).getReleaseById(release.getId())).withSelfRel());
        links.add(linkTo(methodOn(ReleaseController.class).getAllReleases(null, null, null, 0, 20)).withRel("collection"));
        links.add(linkTo(methodOn(ReleaseController.class).patchRelease(release.getId(), null)).withRel("patch"));
        links.add(linkTo(methodOn(ReleaseController.class).deleteRelease(release.getId())).withRel("delete"));

        ReleaseRecord record = storage.releases.get(release.getId());
        if (record != null && record.getArtistId() != null) {
            links.add(linkTo(methodOn(ArtistController.class).getArtistById(record.getArtistId())).withRel("artist"));
        }

        return EntityModel.of(release, links);
    }
}
