package io.chillplus.boundary;

import io.chillplus.entity.CreateTvShowRequest;
import io.chillplus.entity.TvShow;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/api/tv")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TvShowResource {
    @Inject
    Logger log;

    private Set<TvShow> tvShows = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    @GET
    public Set<TvShow> getAll() {
        log.info("getAll: stubbed out, responding with empty list");
        return tvShows;
    }

    @POST
    public Response create(@Valid CreateTvShowRequest newShow) {
        if (!isUnique(newShow)) {
            return Response.ok().status(Response.Status.CONFLICT).build();
        } else {
            tvShows.add(new TvShow(nextId(), newShow.getTitle(), null));
            return Response.created(URI.create("/api/tv"))
                .build();
        }
    }

    @DELETE
    public Response deleteAll() {
        tvShows.clear();
        return Response.noContent().build();
    }


    @Path("{id}")
    @GET
    public Response getOneById(@Context final UriInfo info, @PathParam("id") final Long id) {
        return tvShows.stream()
            .filter(s -> s.getId().equals(id))
            .findFirst()
            .map(x -> Response.ok(x).links(makeLinks(this, info)).build())
            .orElse(Response.ok().status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") final Long id) {
        tvShows = tvShows.stream()
            .filter(s -> !s.getId().equals(id))
            .collect(Collectors.toSet());
        return Response.noContent().build();
    }

    private boolean isUnique(final CreateTvShowRequest request) {
        return isUniqueTitle(request) && ((request.getId() == null) || isUniqueId(request));
    }

    private boolean isUniqueTitle(final CreateTvShowRequest request) {
        final Set<String> collect = tvShows.stream()
            .map(TvShow::getTitle)
            .collect(Collectors.toSet());
        return !collect.contains(request.getTitle());
    }

    private boolean isUniqueId(final CreateTvShowRequest request) {
        final Long id = request.getId();
        return (id == null) || (
            tvShows.stream()
                .map(TvShow::getId)
                .anyMatch(x -> x.equals(request.getId()))
        );
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

    private Long nextId() {
        return tvShows.stream()
            .map(TvShow::getId)
            .max(Comparator.comparing(Long::valueOf))
            .map(x -> x + 1L)
            .orElse(1L);
    }
}
