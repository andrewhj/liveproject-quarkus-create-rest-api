package io.chillplus.boundary;

import io.chillplus.control.TvShowService;
import io.chillplus.entity.CreateTvShowRequest;
import io.chillplus.entity.TvShow;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

@Path(Endpoints.TV_SHOW)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TvShowResource {
    @Inject
    Logger log;
    @Inject
    TvShowService tvShowService;

    @GET
    public Set<TvShow> getAll() {
        log.info("getAll: stubbed out, responding with empty list");
        return tvShowService.findAll();
    }

    @POST
    public Response create(@Valid CreateTvShowRequest newShow) {
        if (newShow.getId() != null) {
            return Response.ok().status(Response.Status.BAD_REQUEST).build();
        } else {
            tvShowService.create(newShow);
            return Response.created(URI.create("/api/tv")).build();
        }
    }

    @DELETE
    public Response deleteAll() {
        tvShowService.clear();
        return Response.noContent().build();
    }


    @Path("{id}")
    @GET
    public Response getOneById(@Context final UriInfo info, @PathParam("id") final Long id) {
        return tvShowService.findById(id)
            .map(x -> Response.ok(x).links(makeLinks(this, info)).build())
            .orElse(Response.ok().status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") final Long id) {
        tvShowService.deleteById(id);
        return Response.noContent().build();
    }

    protected Link[] makeLinks(TvShowResource resource, UriInfo info) {
        return new Link[]{
            Link.fromUri(
                UriBuilder.fromResource(TvShowResource.class)
                    .path(TvShowResource.class, "getOneById")
                    .build("_self"))
                .build("_links")
        };
    }

}
