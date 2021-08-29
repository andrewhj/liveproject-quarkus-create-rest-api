package io.chillplus.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;

public class CreateTvShowRequest {
    final Long id;

    @NotBlank
    final String title;

    @JsonCreator
    public CreateTvShowRequest(final Long id, @NotBlank final String title) {
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
