package io.chillplus.control;

import io.chillplus.entity.CreateTvShowRequest;
import io.chillplus.entity.TvShow;

import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class TvShowService {
    private Set<TvShow> tvShows = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    public Set<TvShow> findAll() {
        return tvShows;
    }

    public void create(final CreateTvShowRequest newShow) {
        tvShows.add(new TvShow(nextId(), newShow.getTitle(), null));
    }

    public void clear() {
        tvShows.clear();
    }

    private Long nextId() {
        return tvShows.stream()
            .map(TvShow::getId)
            .max(Comparator.comparing(Long::valueOf))
            .map(x -> x + 1L)
            .orElse(1L);
    }

    public Optional<TvShow> findById(final Long id) {
        return tvShows.stream()
            .filter(x -> x.getId().equals(id))
            .findFirst();
    }

    public void deleteById(final Long id) {
        tvShows = tvShows.stream()
            .filter(s -> !s.getId().equals(id))
            .collect(Collectors.toSet());
    }

    public boolean isUniqueTitle(final CreateTvShowRequest request) {
        final Set<String> collect = tvShows.stream()
            .map(TvShow::getTitle)
            .collect(Collectors.toSet());
        return !collect.contains(request.getTitle());
    }
}
