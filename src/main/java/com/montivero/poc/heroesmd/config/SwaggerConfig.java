package com.montivero.poc.heroesmd.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Default location
 * Json: /v2/api-docs
 * UI: /swagger-ui/
 */
@Configuration
public class SwaggerConfig {

   @Bean
   public Docket api() {
      List<SecurityScheme> securitySchemes = List.of(new BasicAuth("BasicAuth"));
      List<SecurityReference> basicAuth = List.of(SecurityReference.builder()
                                                                   .reference("BasicAuth")
                                                                   .scopes(new AuthorizationScope[0])
                                                                   .build());
      SecurityContext securityContext = SecurityContext.builder()
                                                       .securityReferences(basicAuth)
                                                       .build();

      ApiInfo apiInfo = new ApiInfoBuilder()
            .title("HeroesMd - Swagger")
            .version("0.1")
            .build();

      return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.montivero.poc.heroesmd.controller"))
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(securitySchemes)
            .securityContexts(List.of(securityContext))
            .apiInfo(apiInfo);
   }

}
