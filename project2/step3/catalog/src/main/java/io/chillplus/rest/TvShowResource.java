package io.chillplus.rest;

import io.chillplus.domain.TvShow;
import io.quarkus.panache.common.Page;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/api/tv")
@Produces("application/json")
@Consumes("application/json")
public class TvShowResource {

    @POST
    @Transactional
    public Response create(@Valid TvShow tvShow) {
        if (tvShow.id != null) {
            throw new WebApplicationException("A new entity cannot already have an ID", Response.Status.BAD_REQUEST);
        }
        tvShow.persist();
        return Response.status(Response.Status.CREATED).entity(tvShow).build();
    }

    @GET
    public List<TvShow> getAll() {
        return TvShow.findAllOrderByTitle();
    }

    @DELETE
    @Transactional
    public Response deleteAll() {
        TvShow.deleteAll();
        return Response.noContent().build();
    }

    @GET
    @Path("{id}")
    public TvShow getOneById(@PathParam("id") long id) {
        final TvShow entity = TvShow.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Entity does not exist. ", Response.Status.NOT_FOUND);
        }
        return entity;
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") final Long id, @Valid TvShow tvShow) {
        if (tvShow.id == null) {
            throw new WebApplicationException("Entity was missing id. ", Response.Status.BAD_REQUEST);
        }
        final TvShow updated = TvShow.findById(id);
        if (updated == null) {
            throw new WebApplicationException("Entity doesn't exist. ", Response.Status.NOT_FOUND);
        }
        updated.category = tvShow.category;
        updated.title = tvShow.title;
        updated.persist();
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteOne(@PathParam("id") long id) {
        TvShow.deleteById(id);
        return Response.noContent().build();
    }

    @GET
    @Path("search/{title}")
    public TvShow getOneByTitle(@PathParam("title") final String title) {
        final TvShow result = TvShow.findByTitle(title);
        if (result == null) {
            throw new WebApplicationException("No entry for title " + title, Response.Status.NOT_FOUND);
        }
        return result;
    }

    @GET
    @Path("categories/{category}")
    public List<TvShow> getAllByCategory(@PathParam("category") final String category, @QueryParam("pageIndex") final Integer pageIndex, @QueryParam("pageSize") final Integer pageSize) {
        if(pageIndex != null && pageSize == null) {
//            if(pageSize == null) {
                throw new WebApplicationException("pageIndex query requires a pageSize", Response.Status.BAD_REQUEST);
//            }
//            else {
//                return TvShow.findByCategoryIgnoreCase(category, Page.of(pageIndex, pageSize));
//            }
        }

        if(pageSize != null) {
            final int index = pageIndex != null ? pageIndex : 0;
            return TvShow.findByCategoryIgnoreCase(category, Page.of(index, pageSize));
        }
        return TvShow.findByCategoryIgnoreCase(category);
    }

}
