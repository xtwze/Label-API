package edu.rutmiit.demo.labelrest.controller;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Единственная точка входа клиента (уровень зрелости REST 3 — HATEOAS).
 * Клиент знает наизусть только GET /api, остальные URL получает из _links.
 */
@RestController
public class RootController {

    @GetMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
    public RepresentationModel<?> root() {
        RepresentationModel<?> root = new RepresentationModel<>();

        root.add(linkTo(methodOn(ArtistController.class).getAllArtists(0, 20)).withRel("artists"));

        String searchHref = linkTo(ArtistController.class).slash("search").toUri() + "{?name,page,size}";
        root.add(Link.of(searchHref, "artists-search"));

        // Шаблонная ссылка на релизы, аналогично примеру из лекции:
        // .../api/releases?page=0&size=20{&artistId,genre,titleSearch}
        String releasesHref = linkTo(methodOn(ReleaseController.class)
                .getAllReleases(null, null, null, 0, 20)).toUri()
                + "{&artistId,genre,titleSearch}";
        root.add(Link.of(releasesHref, "releases"));

        root.add(linkTo(methodOn(ReleaseController.class).getAllReleasesSummary(0, 20)).withRel("releases-summary"));

        return root;
    }
}
