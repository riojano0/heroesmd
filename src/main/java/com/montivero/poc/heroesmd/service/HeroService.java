package com.montivero.poc.heroesmd.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.montivero.poc.heroesmd.domain.api.HeroRequest;
import com.montivero.poc.heroesmd.domain.api.HeroResponse;
import com.montivero.poc.heroesmd.domain.api.PageContainer;
import com.montivero.poc.heroesmd.domain.entity.HeroEntity;
import com.montivero.poc.heroesmd.domain.entity.SkillEntity;
import com.montivero.poc.heroesmd.repository.HeroRepository;
import com.montivero.poc.heroesmd.repository.SkillRepository;
import com.montivero.poc.heroesmd.repository.specification.HeroSpecificationBuilder;
import com.montivero.poc.heroesmd.service.transformer.HeroMapper;

@Service
public class HeroService {

   // Eg: name:manolo,realName:NoManolo ->  ("name", ":", "manolo") ("realName", ":", "NoManolo")
   private static final Pattern SEARCH_PATTERN = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");

   private final HeroRepository heroRepository;
   private final SkillRepository skillRepository;

   @Autowired
   public HeroService(HeroRepository heroRepository, SkillRepository skillRepository) {
      this.heroRepository = heroRepository;
      this.skillRepository = skillRepository;
   }

   public List<HeroResponse> getAllHeroes() {
      List<HeroEntity> heroEntities = heroRepository.findAll();

      return heroEntities.stream()
                         .map(HeroMapper.INSTANCE::toResponse)
                         .collect(Collectors.toList());
   }

   public PageContainer<HeroResponse> getAllHeroesSlice(Integer page, Integer size, Sort.Direction direction) {
      PageRequest pageRequest = PageRequest.of(page, size, direction, "id");
      Slice<HeroEntity> entitySlice = heroRepository.findBy(pageRequest);

      return getHeroResponsePageContainer(page, size, entitySlice);
   }

   public PageContainer<HeroResponse> searchHeroes(String search, Integer page, Integer size, Sort.Direction direction) {

      HeroSpecificationBuilder builder = new HeroSpecificationBuilder();
      Matcher matcher = SEARCH_PATTERN.matcher(search + ",");
      while (matcher.find()) {
         String key = matcher.group(1);
         String operator = matcher.group(2);
         String value = matcher.group(3);
         builder.with(key, operator, value);
      }

      PageRequest pageRequest = PageRequest.of(page, size, direction, "id");
      Specification<HeroEntity> heroEntitySpecification = builder.build();
      Page<HeroEntity> heroEntities = heroRepository.findAll(heroEntitySpecification, pageRequest);
      return getHeroResponsePageContainer(page, size, heroEntities);
   }

   public HeroResponse save(HeroRequest heroRequest) {
      String name = heroRequest.getName();
      validateNameAlreadyExists(name);

      HeroEntity heroEntity = HeroMapper.INSTANCE.toEntity(heroRequest);
      List<SkillEntity> skillEntities = ListUtils.emptyIfNull(heroEntity.getSkills())
                                                 .stream()
                                                 .map(entity -> {
                                                   Optional<SkillEntity> byNameIgnoreCase = skillRepository
                                                         .findByNameIgnoreCase(entity.getName());

                                                    return byNameIgnoreCase.orElse(entity);
                                                 }).collect(Collectors.toList());
      heroEntity.setSkills(skillEntities);

      HeroEntity heroEntitySaved = heroRepository.save(heroEntity);

      return HeroMapper.INSTANCE.toResponse(heroEntitySaved);
   }

   public HeroResponse editHero(Long id, HeroRequest heroRequest) {

      Optional<HeroEntity> optionalHeroEntity = heroRepository.findById(id);

      String name = heroRequest.getName();
      validateNameAlreadyExists(name);

      String invalidMessage = MessageFormat.format("Invalid Hero ID: {0}", id);
      HeroEntity heroEntity = optionalHeroEntity.orElseThrow(() -> new ValidationException(invalidMessage));

      HeroEntity heroEntityChanges = HeroMapper.INSTANCE.toEntity(heroRequest);
      HeroEntity heroEntityUpdated = mergeToCurrentEntity(heroEntity, heroEntityChanges);
      HeroEntity heroEntitySaved = heroRepository.save(heroEntityUpdated);

      return HeroMapper.INSTANCE.toResponse(heroEntitySaved);
   }

   public boolean deleteHero(Long id) {
      if (heroRepository.existsById(id)) {
         heroRepository.deleteById(id);
         return true;
      }

      return false;
   }

   public Optional<HeroResponse> getHeroById(Long id) {
      Optional<HeroEntity> heroById = heroRepository.findById(id);

      return heroById.map(HeroMapper.INSTANCE::toResponse);
   }

   private void validateNameAlreadyExists(String name) {
      Optional<HeroEntity> optionalCurrentHero = heroRepository.findByName(name);

      if (optionalCurrentHero.isPresent()) {
         throw new ValidationException("Hero with same name already exists: " + name);
      }
   }

   private static PageContainer<HeroResponse> getHeroResponsePageContainer(Integer page, Integer size, Slice<HeroEntity> entitySlice) {
      List<HeroEntity> content = ListUtils.emptyIfNull(entitySlice.getContent());
      List<HeroResponse> heroResponses = content.stream()
                                                .map(HeroMapper.INSTANCE::toResponse)
                                                .collect(Collectors.toList());

      return PageContainer.<HeroResponse>builder()
            .content(heroResponses)
            .size(size)
            .page(page)
            .lastPage(entitySlice.isLast())
            .build();
   }

   private HeroEntity mergeToCurrentEntity(HeroEntity heroEntity, HeroEntity heroEntityChanges) {
      Optional.ofNullable(heroEntityChanges.getName())
              .ifPresent(heroEntity::setName);
      Optional.ofNullable(heroEntityChanges.getDescription())
              .ifPresent(heroEntity::setDescription);
      Optional.ofNullable(heroEntityChanges.getImageUrl())
              .ifPresent(heroEntity::setImageUrl);
      Optional.ofNullable(heroEntityChanges.getRealName())
              .ifPresent(heroEntity::setRealName);

      List<SkillEntity> skills = heroEntityChanges.getSkills();
      if (CollectionUtils.isNotEmpty(skills)) {
         List<SkillEntity> skillEntities = new ArrayList<>();
         for (SkillEntity skillsToSave : skills) {
            Optional<SkillEntity> byNameIgnoreCase = skillRepository.findByNameIgnoreCase(skillsToSave.getName());

            if (byNameIgnoreCase.isPresent()) {
               skillEntities.add(byNameIgnoreCase.get());
            } else {
               skillEntities.add(skillsToSave);
            }
         }
         heroEntity.setSkills(skillEntities);
      }

      return heroEntity;
   }

}
