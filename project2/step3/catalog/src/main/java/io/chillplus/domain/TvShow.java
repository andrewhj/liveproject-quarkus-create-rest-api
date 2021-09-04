package io.chillplus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Entity
@Table(name = "tv_show")
public class TvShow extends PanacheEntity {

    @NotBlank
    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "category")
    public String category;

    public static List<TvShow> findAllOrderByTitle() {
        return listAll(Sort.by("title"));
    }

    public static TvShow findByTitle(final String title) {
        return find("title", title).firstResult();
    }

    public static List<TvShow> findByCategoryIgnoreCase(final String category) {
        return findByCategoryIgnoreCase(category, Page.ofSize(25));
    }
    public static List<TvShow> findByCategoryIgnoreCase(final String category, Page page) {
        final PanacheQuery<TvShow> categoryQuery = find("LOWER(category) = LOWER(:category)", Parameters.with("category", category));
        final PanacheQuery<TvShow> queryPage = categoryQuery.page(page);
        return queryPage.list();
    }
}
