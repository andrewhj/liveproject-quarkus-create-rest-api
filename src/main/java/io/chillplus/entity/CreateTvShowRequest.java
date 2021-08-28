package io.chillplus.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotEmpty;

public class CreateTvShowRequest {
    final Long id;

    @NotEmpty
    final String title;

    @JsonCreator
    public CreateTvShowRequest(final Long id, @NotEmpty final String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }
}
