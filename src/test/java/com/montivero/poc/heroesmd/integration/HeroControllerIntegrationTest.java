package com.montivero.poc.heroesmd.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import com.montivero.poc.heroesmd.domain.api.HeroRequest;
import com.montivero.poc.heroesmd.domain.api.HeroResponse;

@SpringBootTest(
   webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(locations = "classpath:application-test.properties")
public class HeroControllerIntegrationTest {

   @LocalServerPort
   private Integer port;

   private RequestSpecification requestSpecification;

   @BeforeEach
   void setUp() {
      requestSpecification = new RequestSpecBuilder()
            .setPort(port)
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
      assertThat(heroResponses[0].getName(), is("ManolitonTest"));
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
      heroRequest.setName("NewHero");

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
}
