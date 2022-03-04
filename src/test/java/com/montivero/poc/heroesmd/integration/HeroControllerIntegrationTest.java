package com.montivero.poc.heroesmd.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.time.Instant;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.montivero.poc.heroesmd.domain.api.HeroRequest;
import com.montivero.poc.heroesmd.domain.api.HeroResponse;

@SpringBootTest(
   webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(locations = "classpath:application-test.properties", properties = {
      "HEROES-USER=user",
      "HEROES-PASSWORD=pass1"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class HeroControllerIntegrationTest {

   @LocalServerPort
   private Integer port;

   private RequestSpecification requestSpecification;

   @BeforeEach
   void setUp() {
      PreemptiveBasicAuthScheme authorization = new PreemptiveBasicAuthScheme();
      authorization.setUserName("user");
      authorization.setPassword("pass1");
      requestSpecification = new RequestSpecBuilder()
            .setPort(port)
            .setAuth(authorization)
            .build();

      RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.BODY);
   }

   @Test
   void shouldGetAllHeroes() {
      Response response = RestAssured
            .given(requestSpecification)
               .contentType("application/json")
            .when()
               .get("/hero/all")
            .then()
               .log()
               .all()
               .extract().response();

      assertThat(response.getStatusCode(), is(200));
      HeroResponse[] heroResponses = response.getBody().as(HeroResponse[].class);
      assertThat(heroResponses.length, is(2));
      assertThat(heroResponses[0].getId(), is(1L));
      assertThat(heroResponses[0].getName(), is("ManolitonTest"));
      assertThat(heroResponses[1].getId(), is(2L));
      assertThat(heroResponses[1].getName(), is("JavieronTest"));
   }

   @Test
   void shouldGetAllHeroesWithoutId() {
      RequestSpecification requestSpecificationWithoutCredentials = new RequestSpecBuilder()
            .setPort(port)
            .build();
      Response response = RestAssured
            .given(requestSpecificationWithoutCredentials)
            .contentType("application/json")
            .when()
            .get("/hero/all")
            .then()
            .log()
            .all()
            .extract().response();

      assertThat(response.getStatusCode(), is(200));
      HeroResponse[] heroResponses = response.getBody().as(HeroResponse[].class);
      assertThat(heroResponses.length, is(2));
      assertThat(heroResponses[0].getId(), nullValue());
      assertThat(heroResponses[0].getName(), is("ManolitonTest"));
      assertThat(heroResponses[1].getId(), nullValue());
      assertThat(heroResponses[1].getName(), is("JavieronTest"));
   }

   @Test
   void shouldCreateHero() {
      HeroRequest heroRequest = new HeroRequest();
      heroRequest.setName("NewHero");

      Response response = RestAssured
            .given(requestSpecification)
            .contentType("application/json")
            .when()
               .body(heroRequest)
               .post("/hero")
            .then()
               .log()
               .all()
               .extract().response();

      assertThat(response.getStatusCode(), is(200));
      HeroResponse heroResponses = response.getBody().as(HeroResponse.class);
      assertThat(heroResponses.getId(), notNullValue());
      assertThat(heroResponses.getName(), is("NewHero"));
   }

   @Test
   void shouldDeleteHeroCreatedHero() {
      HeroRequest heroRequest = new HeroRequest();
      heroRequest.setName("NewHero" + Instant.now());

      Response preResponse = RestAssured
            .given(requestSpecification)
            .contentType("application/json")
            .when()
            .body(heroRequest)
            .post("/hero")
            .then()
            .log()
            .all()
            .extract().response();
      HeroResponse heroResponses = preResponse.getBody().as(HeroResponse.class);

      Response response = RestAssured
            .given(requestSpecification)
            .contentType("application/json")
            .when()
               .pathParam("id", heroResponses.getId())
               .delete("/hero/{id}")
            .then()
            .log()
            .all()
            .extract().response();

      assertThat(response.getStatusCode(), is(200));
      Map<String, Boolean> result = response.getBody().as(Map.class);
      assertThat(result.get("deleted"), is(true));
   }

   @Test
   void shouldFailWith401ToCreateHeroWhenCredentialsAreInvalid() {
      RequestSpecification requestSpecificationWithoutCredentials = new RequestSpecBuilder()
            .setPort(port)
            .build();
      HeroRequest heroRequest = new HeroRequest();
      heroRequest.setName("NewHero");

      Response response = RestAssured
            .given(requestSpecificationWithoutCredentials)
            .contentType("application/json")
            .when()
            .body(heroRequest)
            .post("/hero")
            .then()
            .log()
            .all()
            .extract().response();

      assertThat(response.getStatusCode(), is(401));
   }
}
