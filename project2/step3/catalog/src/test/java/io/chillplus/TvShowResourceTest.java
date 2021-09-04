package io.chillplus;

import io.chillplus.domain.TvShow;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class TvShowResourceTest {

    public static final String DEFAULT_TITLE = "AAA";
    public static final String TV_ENDPOINT = "/api/tv";

    @BeforeEach
    public void beforeEach() {
        given()
                .when()
                .delete(TV_ENDPOINT)
                .then()
                .statusCode(204);
    }

    @Test
    public void createTvShow() {
        given()
                .when()
                .get(TV_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("$.size()", is(0));

        TvShow tvShow = new TvShow();
        tvShow.title = DEFAULT_TITLE;

        given()
                .body(tvShow)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .post(TV_ENDPOINT)
                .then()
                .statusCode(201)
                .contentType(APPLICATION_JSON)
                .body("title", is(tvShow.title));

        given()
                .when()
                .get(TV_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("$.size()", is(1));

        TvShow tvShowWithId = new TvShow();
        tvShow.id = 1L;
        tvShow.title = DEFAULT_TITLE;

        given()
                .body(tvShowWithId)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .post(TV_ENDPOINT)
                .then()
                .statusCode(400);
    }

    @Test
    public void checkTvShowTitleIsNotBlank() {
        given()
                .when()
                .get(TV_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("$.size()", is(0));

        TvShow tvShow = new TvShow();
        tvShow.title = "";

        given()
                .body(tvShow)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .post(TV_ENDPOINT)
                .then()
                .statusCode(400);

        given()
                .when()
                .get(TV_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("$.size()", is(0));
    }

    @Test
    public void getAllTvShows() {
        given()
                .when()
                .get(TV_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("$.size()", is(0));

        TvShow bbShow = new TvShow();
        bbShow.title = "AA";

        given()
                .body(bbShow)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .post(TV_ENDPOINT)
                .then()
                .statusCode(201)
                .contentType(APPLICATION_JSON)
                .body("title", is(bbShow.title));

        TvShow aaShow = new TvShow();
        aaShow.title = "BB";

        given()
                .body(aaShow)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .post(TV_ENDPOINT)
                .then()
                .statusCode(201)
                .contentType(APPLICATION_JSON)
                .body("title", is(aaShow.title));

        given()
                .when()
                .get(TV_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("$.size()", is(2));
    }

    @Test
    public void getAllTvShowsOrderByTitle() {
        final TvShow firstShow = new TvShow();
        firstShow.title = "BB";
        assertCreated(firstShow);

        final TvShow secondShow = new TvShow();
        secondShow.title = "AA";
        assertCreated(secondShow);

        final List<TvShow> shows = given()
                .when()
                .get(TV_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("$.size()", is(2))
                .extract().jsonPath().getList("", TvShow.class);
        assertThat(shows.get(0).title, is(secondShow.title));
    }

    @Test
    public void getTvShowByTitle() {
        final TvShow aaa = new TvShow();
        aaa.title = "AAA";
        final TvShow createdShow = assertCreated(aaa);

        final TvShow result = given()
                .when()
                .get(TV_ENDPOINT + "/search/{title}", createdShow.title)
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .extract().as(TvShow.class);
        assertThat(result.id, is(createdShow.id));
    }

    private TvShow assertCreated(final TvShow show) {
        return given()
                .body(show)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .post(TV_ENDPOINT)
                .then()
                .statusCode(201)
                .contentType(APPLICATION_JSON)
                .body("title", is(show.title))
                .extract().as(TvShow.class);
    }

    @Test
    public void getOneTvShow() {
        given()
                .when()
                .get(TV_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("$.size()", is(0));

        TvShow tvShow = new TvShow();
        tvShow.title = DEFAULT_TITLE;

        final TvShow result = given()
                .body(tvShow)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .post(TV_ENDPOINT)
                .then()
                .statusCode(201)
                .contentType(APPLICATION_JSON)
                .body("title", is(tvShow.title))
                .extract().as(TvShow.class);

        given()
                .when()
                .get("/api/tv/{id}", result.id)
                .then()
                .statusCode(200);
    }

    @Test
    public void getNonExistingTvShow() {
        given()
                .when()
                .get("/api/tv/42")
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteAllTvShows() {
        TvShow tvShow = new TvShow();
        tvShow.title = DEFAULT_TITLE;

        given()
                .body(tvShow)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .post(TV_ENDPOINT)
                .then()
                .statusCode(201)
                .contentType(APPLICATION_JSON)
                .body("title", is(tvShow.title));

        given()
                .when()
                .get(TV_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("$.size()", is(1));

        given()
                .when()
                .delete(TV_ENDPOINT)
                .then()
                .statusCode(204);

        given()
                .when()
                .get(TV_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("$.size()", is(0));
    }

    @Test
    public void deleteOneTvShow() {
        TvShow tvShow = new TvShow();
        tvShow.title = DEFAULT_TITLE;

        given()
                .body(tvShow)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .post(TV_ENDPOINT)
                .then()
                .statusCode(201)
                .contentType(APPLICATION_JSON)
                .body("title", is(tvShow.title));

        given()
                .when()
                .get(TV_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("$.size()", is(1));

        given()
                .when()
                .delete(TV_ENDPOINT)
                .then()
                .statusCode(204);

        given()
                .when()
                .get(TV_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("$.size()", is(0));
    }

    @Test
    public void updateTvShow() {
        final TvShow tvShow = new TvShow();
        tvShow.title = DEFAULT_TITLE;

        final TvShow savedShow = given()
                .body(tvShow)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .post(TV_ENDPOINT)
                .then()
                .statusCode(201)
                .contentType(APPLICATION_JSON)
                .body("title", is(tvShow.title))
                .extract().as(TvShow.class);

        savedShow.title = "BBB";
        given()
                .body(savedShow)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .put("/api/tv/{id}", savedShow.id)
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("title", is(savedShow.title))
        ;

        final TvShow tvShowNoId = new TvShow();
        tvShowNoId.title = "CCC";
        tvShowNoId.category = savedShow.category;

        given()
                .body(tvShowNoId)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .when()
                .put("/api/tv/{id}", savedShow.id)
                .then()
                .statusCode(400);
    }

    @Test
    public void getAllTvShowsByCategory() {
        final TvShow tvShow = new TvShow();
        tvShow.title = "aj";
        tvShow.category = "first";

        final TvShow createdShow = assertCreated(tvShow);
        given()
                .when()
                .get(TV_ENDPOINT + "/categories/{category}", "first")
                .then()
                .statusCode(200)
                .body("$.size()", is(1));

        given()
                .when()
                .get(TV_ENDPOINT + "/categories/{category}", "FIRST")
                .then()
                .statusCode(200)
                .body("$.size()", is(1));


        given()
                .when()
                .get(TV_ENDPOINT + "/categories/{category}", "non-existent")
                .then()
                .statusCode(200)
                .body("$.size()", is(0));

        for (int i = 1; i <= 30; ++i) {
            final TvShow newShow = new TvShow();
            newShow.title = "aj-" + i;
            newShow.category = "second";
            assertCreated(newShow);
        }

        given()
                .when()
                .get(TV_ENDPOINT + "/categories/{category}", "second")
                .then()
                .statusCode(200)
                .body("$.size()", is(25));

        given()
                .when()
                .get(TV_ENDPOINT + "/categories/{category}?pageSize={pageSize}", "second", 25)
                .then()
                .statusCode(200)
                .body("$.size()", is(25));

        given()
                .when()
                .get(TV_ENDPOINT + "/categories/{category}?pageSize={pageSize}", "second", 50)
                .then()
                .statusCode(200)
                .body("$.size()", is(30));

        given()
                .when()
                .get(TV_ENDPOINT + "/categories/{category}?pageIndex={pageIndex}", "second", 10)
                .then()
                .statusCode(400);

        given()
                .when()
                .get(TV_ENDPOINT + "/categories/{category}?pageSize={pageSize}&pageIndex={pageIndex}", "second", 25, 0)
                .then()
                .statusCode(200)
                .body("$.size()", is(25));

        given()
                .when()
                .get(TV_ENDPOINT + "/categories/{category}?pageSize={pageSize}&pageIndex={pageIndex}", "second", 25, 1)
                .then()
                .statusCode(200)
                .body("$.size()", is(5));

        given()
                .when()
                .get(TV_ENDPOINT + "/categories/{category}?pageSize={pageSize}&pageIndex={pageIndex}", "second", 25, 2)
                .then()
                .statusCode(200)
                .body("$.size()", is(0));
    }
}