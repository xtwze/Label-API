package edu.rutmiit.demo.labelrest.assembler;

import edu.rutmiit.demo.booksapicontract.dto.Responses.ReleaseSummaryResponse;
import edu.rutmiit.demo.labelrest.controller.ReleaseController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/** Задание 5: ассемблер для облегчённого каталога релизов. */
@Component
public class ReleaseSummaryModelAssembler
        implements RepresentationModelAssembler<ReleaseSummaryResponse, EntityModel<ReleaseSummaryResponse>> {

    @Override
    public EntityModel<ReleaseSummaryResponse> toModel(ReleaseSummaryResponse summary) {
        return EntityModel.of(summary,
                // self ведёт на ПОЛНЫЙ ресурс релиза, как и требует задание
                linkTo(methodOn(ReleaseController.class).getReleaseById(summary.getId())).withSelfRel(),
                linkTo(methodOn(ReleaseController.class).getAllReleasesSummary(0, 20)).withRel("collection")
        );
    }
}
