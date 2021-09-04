package io.chillplus.boundary;

import io.chillplus.control.TvShowService;
import io.chillplus.entity.CreateTvShowRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class TvShowResourceTest {
    final Headers headers = new Headers(new Header("Content-Type", "application/json"), new Header("Accept", "application/json"));
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    TvShowService tvShowService;

    @BeforeEach
    public void clearRepo() {
        tvShowService.clear();
    }

    @Test
    public void getAllTvShowsEmptyInitially() {
        //@formatter:off
        given()
        .when()
            .get(Endpoints.TV_SHOW)
        .then()
            .statusCode(200)
            .body("", equalTo(Collections.emptyList()));
        //@formatter:on
    }

    @Test
    public void checkTvShowTitleIsNotBlank() {
        //@formatter:off
        given()
            .headers(headers)
            .body(new CreateTvShowRequest(""))
        .when()
            .post(Endpoints.TV_SHOW)
        .then()
            .contentType(JSON)
            .statusCode(400)
            .body("parameterViolations.message", contains("must not be blank"));
        //@fomatter:on
    }

    @Test
    public void createTvShowCreatesWhenValidRequest(){
        //@formatter:off
        given()
            .headers(headers)
            .body(new CreateTvShowRequest( "Test Movie"))
        .when()
            .post(Endpoints.TV_SHOW)
        .then()
            .statusCode(201);
        //@formatter:on
    }

    @Test
    public void createTvShowFailsWhenIdIsPassed() {
        //@formatter:off
        given()
            .headers(headers)
            .body(new CreateTvShowRequest(1L, "Test Movie"))
        .when()
            .post(Endpoints.TV_SHOW)
        .then()
            .statusCode(400);
        //@formatter:on
    }

    @Test
    public void getAllTvShowsReturnsResultsWhenExisting() {
        tvShowService.create(new CreateTvShowRequest("First"));
        tvShowService.create(new CreateTvShowRequest("Second"));
        //@formatter:off
        given()
            .headers(headers)
        .when()
            .get(Endpoints.TV_SHOW)
        .then()
            .contentType(JSON)
            .statusCode(200)
            .body("title", hasItems("First", "Second"));
        //@formatter:on
    }

    @Test
    public void getOneTvShowReturnsWhenFound() {
        tvShowService.create(new CreateTvShowRequest("First"));
        //@formatter:off
        given()
            .headers(headers)
        .when()
            .get(Endpoints.TV_SHOW + "/{id}",  1)
        .then()
            .contentType(JSON)
            .statusCode(200)
            .body("title", equalTo("First"),
                "id", equalTo(1));
        //@formatter:on
    }

    @Test
    public void getNonExistingTvShow() {
        //@formatter:off
        given()
        .when()
            .get(Endpoints.TV_SHOW + "/{id}", 42)
        .then()
            .statusCode(404);
        //@formatter:on
    }

    @Test
    public void deleteAllTvShows() {
        //@formatter:off
        given()
        .when()
            .delete(Endpoints.TV_SHOW)
        .then()
            .statusCode(204);
        //@formatter:on
    }

    @Test
    public void deleteOneTvShow() {
        tvShowService.create(new CreateTvShowRequest("First"));
        //@formatter:off
        given()
        .when()
            .delete(Endpoints.TV_SHOW + "/{id}", 1)
        .then()
            .statusCode(204);
        //@formatter:on
    }

}
