package io.chillplus.entity;

public class TvShow {
    Long id;
    String title;
    String category;

    public TvShow(final Long id, final String title, final String category) {
        this.id = id;
        this.title = title;
        this.category = category;
    }

    protected TvShow() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }
}
