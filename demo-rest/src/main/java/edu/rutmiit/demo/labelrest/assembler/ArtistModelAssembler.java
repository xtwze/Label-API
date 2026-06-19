package edu.rutmiit.demo.labelrest.assembler;

import edu.rutmiit.demo.booksapicontract.dto.Responses.ArtistResponse;
import edu.rutmiit.demo.labelrest.controller.ArtistController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Собирает EntityModel&lt;ArtistResponse&gt; со ссылками на связанные действия.
 *
 * linkTo(methodOn(...)) НЕ вызывает метод по-настоящему — methodOn создаёт
 * CGLIB-прокси, Spring HATEOAS считывает аннотации маппинга и строит URL.
 * Если переименовать @GetMapping в контроллере — ссылки обновятся сами.
 */
@Component
public class ArtistModelAssembler implements RepresentationModelAssembler<ArtistResponse, EntityModel<ArtistResponse>> {

    @Override
    public EntityModel<ArtistResponse> toModel(ArtistResponse artist) {
        return EntityModel.of(artist,
                linkTo(methodOn(ArtistController.class).getArtistById(artist.getId())).withSelfRel(),
                linkTo(methodOn(ArtistController.class).getAllArtists(0, 20)).withRel("collection"),
                linkTo(methodOn(ArtistController.class).getReleasesByArtist(artist.getId(), 0, 20)).withRel("releases"),
                linkTo(methodOn(ArtistController.class).getArtistProfile(artist.getId())).withRel("profile"),

                // Задание 2: ссылки на полное и частичное обновление, а также на удаление.
                linkTo(methodOn(ArtistController.class).updateArtist(artist.getId(), null)).withRel("update"),
                linkTo(methodOn(ArtistController.class).patchArtist(artist.getId(), null)).withRel("patch"),
                linkTo(methodOn(ArtistController.class).deleteArtist(artist.getId())).withRel("delete")
        );
    }
}
