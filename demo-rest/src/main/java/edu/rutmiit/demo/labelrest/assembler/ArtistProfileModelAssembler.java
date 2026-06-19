package edu.rutmiit.demo.labelrest.assembler;

import edu.rutmiit.demo.booksapicontract.dto.Responses.ArtistProfileResponse;
import edu.rutmiit.demo.labelrest.controller.ArtistController;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Собирает EntityModel для профиля артиста.
 *
 * Это НЕ обычный RepresentationModelAssembler&lt;T, EntityModel&lt;T&gt;&gt;, потому что
 * путь в контракте — /api/artists/{id}/profile, где {id} это ID АРТИСТА,
 * а не ID самого профиля (у профиля своя последовательность ID). Поэтому
 * artistId нужно передавать явно, его нельзя достать из ArtistProfileResponse.id.
 */
@Component
public class ArtistProfileModelAssembler {

    public EntityModel<ArtistProfileResponse> toModel(Long artistId, ArtistProfileResponse profile) {
        return EntityModel.of(profile,
                linkTo(methodOn(ArtistController.class).getArtistProfile(artistId)).withSelfRel(),
                linkTo(methodOn(ArtistController.class).getArtistById(artistId)).withRel("artist"),
                linkTo(methodOn(ArtistController.class).updateArtistProfile(artistId, null)).withRel("update"),
                linkTo(methodOn(ArtistController.class).patchArtistProfile(artistId, null)).withRel("patch")
        );
    }
}
