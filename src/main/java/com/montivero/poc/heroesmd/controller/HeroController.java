package com.montivero.poc.heroesmd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import io.swagger.annotations.Api;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.montivero.poc.heroesmd.aspect.time.LogTimed;
import com.montivero.poc.heroesmd.domain.api.HeroRequest;
import com.montivero.poc.heroesmd.domain.api.HeroResponse;
import com.montivero.poc.heroesmd.domain.api.PageContainer;
import com.montivero.poc.heroesmd.service.HeroService;

@RestController
@RequestMapping("hero")
@Api(value = "Hero controller", description = "Endpoints use for Heroes")
public class HeroController {

   private final HeroService heroService;

   public HeroController(HeroService heroService) {
      this.heroService = heroService;
   }

   @LogTimed
   @GetMapping("/all")
   @Cacheable("cache-by-seconds")
   public List<HeroResponse> getAllHeroes() {
      return heroService.getAllHeroes();
   }


   @LogTimed
   @GetMapping
   @Cacheable("cache-by-seconds")
   public ResponseEntity<HeroResponse> getHero(@RequestParam(value = "id") Long id) {
      Optional<HeroResponse> heroByIdOptional = heroService.getHeroById(id);
      return heroByIdOptional
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
   }

   @LogTimed
   @GetMapping("/page")
   @Cacheable("cache-by-seconds")
   public PageContainer<HeroResponse> getAllHeroesSlice(
         @RequestParam(value = "page", defaultValue = "0") Integer page,
         @RequestParam(value = "size", defaultValue = "10") Integer size,
         @RequestParam(value = "direction", defaultValue = "ASC") Sort.Direction direction) {

      return heroService.getAllHeroesSlice(page, size, direction);
   }

   @LogTimed
   @GetMapping("/search")
   @Cacheable("cache-by-seconds")
   public PageContainer<HeroResponse> searchHeroes(
         @RequestParam(value = "q") String query,
         @RequestParam(value = "page", defaultValue = "0") Integer page,
         @RequestParam(value = "size", defaultValue = "100") Integer size,
         @RequestParam(value = "direction", defaultValue = "ASC") Sort.Direction direction) {

      return heroService.searchHeroes(query, page, size, direction);
   }

   @LogTimed
   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
   public HeroResponse createHero(@RequestBody @Valid HeroRequest heroRequest) {
      return heroService.save(heroRequest);
   }

   @LogTimed
   @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
   public HeroResponse editHero(@PathVariable("id") Long id, @RequestBody @Valid HeroRequest heroRequest) {
      return heroService.editHero(id, heroRequest);
   }

   @LogTimed
   @DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Object> editHero(@PathVariable("id") Long id) {
      boolean deleteHero = heroService.deleteHero(id);

      Map<String, Boolean> result = new HashMap<>();
      result.put("deleted", deleteHero);

      if (deleteHero) {
         return ResponseEntity.ok(result);
      }

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
   }

}
