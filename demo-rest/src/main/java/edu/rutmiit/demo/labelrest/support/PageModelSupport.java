package edu.rutmiit.demo.labelrest.support;

import edu.rutmiit.demo.booksapicontract.dto.Responses.PagedResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.List;
import java.util.function.IntFunction;

/**
 * Мост PagedResponse -> PagedModel&lt;EntityModel&lt;T&gt;&gt; (см. лекцию 2.10).
 *
 * В лекции для этого моста используется связка PageImpl + PagedResourcesAssembler.
 * Здесь это сделано вручную, без подключения spring-data-commons: контроллер сам
 * передаёт функцию "номер страницы -> LinkBuilder на тот же метод контроллера",
 * а этот класс собирает self/first/prev/next/last ссылки и оборачивает контент.
 */
public final class PageModelSupport {

    private PageModelSupport() {
    }

    public static <T extends RepresentationModel<T>> PagedModel<EntityModel<T>> build(
            PagedResponse<T> paged,
            RepresentationModelAssembler<T, EntityModel<T>> assembler,
            IntFunction<LinkBuilder> linkForPage) {

        List<EntityModel<T>> content = paged.content().stream()
                .map(assembler::toModel)
                .toList();

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                paged.pageSize(), paged.pageNumber(), paged.totalElements(), paged.totalPages());

        PagedModel<EntityModel<T>> model = PagedModel.of(content, metadata);

        int lastPage = Math.max(paged.totalPages() - 1, 0);
        model.add(linkForPage.apply(paged.pageNumber()).withRel(IanaLinkRelations.SELF));
        model.add(linkForPage.apply(0).withRel(IanaLinkRelations.FIRST));
        model.add(linkForPage.apply(lastPage).withRel(IanaLinkRelations.LAST));
        if (paged.pageNumber() > 0) {
            model.add(linkForPage.apply(paged.pageNumber() - 1).withRel(IanaLinkRelations.PREV));
        }
        if (paged.pageNumber() < lastPage) {
            model.add(linkForPage.apply(paged.pageNumber() + 1).withRel(IanaLinkRelations.NEXT));
        }
        return model;
    }
}
