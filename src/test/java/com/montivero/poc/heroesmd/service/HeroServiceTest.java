package com.montivero.poc.heroesmd.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.montivero.poc.heroesmd.domain.api.HeroRequest;
import com.montivero.poc.heroesmd.domain.api.HeroResponse;
import com.montivero.poc.heroesmd.domain.api.PageContainer;
import com.montivero.poc.heroesmd.domain.api.Skill;
import com.montivero.poc.heroesmd.domain.entity.HeroEntity;
import com.montivero.poc.heroesmd.domain.entity.SkillEntity;
import com.montivero.poc.heroesmd.repository.HeroRepository;
import com.montivero.poc.heroesmd.repository.SkillRepository;
import com.montivero.poc.heroesmd.repository.specification.HeroSpecification;
import com.montivero.poc.heroesmd.repository.specification.SearchCriteria;

class HeroServiceTest {

   private HeroRepository heroRepositoryMock;
   private SkillRepository skillRepositoryMock;

   private HeroService heroService;

   @BeforeEach
   void setUp() {
      heroRepositoryMock = Mockito.mock(HeroRepository.class);
      skillRepositoryMock = Mockito.mock(SkillRepository.class);

      heroService = new HeroService(heroRepositoryMock, skillRepositoryMock);
   }

   @Test
   void shouldGetAllTheHeroes() {
      List<HeroEntity> heroEntities = Arrays.asList(
            HeroEntity.builder().name("M0").build(),
            HeroEntity.builder().name("M1").build());
      when(heroRepositoryMock.findAll()).thenReturn(heroEntities);

      List<HeroResponse> allHeroes = heroService.getAllHeroes();

      assertThat(allHeroes, hasSize(2));
      assertThat(allHeroes.get(0).getName(), is("M0"));
      assertThat(allHeroes.get(1).getName(), is("M1"));
      verify(heroRepositoryMock).findAll();
   }

   @Test
   void shouldGetHeroesSliceOnPageContainer() {
      List<HeroEntity> heroEntities = Arrays.asList(
            HeroEntity.builder().name("M0").build(),
            HeroEntity.builder().name("M1").build());
      int page = 0;
      int size = 2;
      Sort.Direction direction = Sort.Direction.ASC;
      PageRequest pageRequest = PageRequest.of(page, size, direction, "id");
      SliceImpl<HeroEntity> heroEntitySlice = new SliceImpl<>(heroEntities, pageRequest, false);

      when(heroRepositoryMock.findBy(pageRequest)).thenReturn(heroEntitySlice);

      PageContainer<HeroResponse> pageContainer = heroService.getAllHeroesSlice(page, size, direction);

      assertThat(pageContainer.getPage(), is(0));
      assertThat(pageContainer.getSize(), is(2));
      assertThat(pageContainer.isLastPage(), is(true));
      List<HeroResponse> content = pageContainer.getContent();
      assertThat(content, hasSize(2));
      assertThat(content.get(0).getName(), is("M0"));
      assertThat(content.get(1).getName(), is("M1"));
      verify(heroRepositoryMock).findBy(Mockito.refEq(pageRequest));
   }

   @Test
   void shouldSearchHeroesSliceOnPageContainer() {
      ArgumentCaptor<Specification<HeroEntity>> specificationCaptor = ArgumentCaptor.forClass(Specification.class);
      List<HeroEntity> heroEntities = Arrays.asList(
            HeroEntity.builder().name("M0").build(),
            HeroEntity.builder().name("M1").build());
      int page = 0;
      int size = 2;
      Sort.Direction direction = Sort.Direction.ASC;
      PageRequest pageRequest = PageRequest.of(page, size, direction, "id");
      PageImpl<HeroEntity> heroEntitySlice = new PageImpl<HeroEntity>(heroEntities, pageRequest, 2);

      when(heroRepositoryMock.findAll(nullable(Specification.class), eq(pageRequest))).thenReturn(heroEntitySlice);

      PageContainer<HeroResponse> pageContainer = heroService.searchHeroes("name:M", page, size, direction);

      assertThat(pageContainer.getPage(), is(0));
      assertThat(pageContainer.getSize(), is(2));
      assertThat(pageContainer.isLastPage(), is(true));
      List<HeroResponse> content = pageContainer.getContent();
      assertThat(content, hasSize(2));
      assertThat(content.get(0).getName(), is("M0"));
      assertThat(content.get(1).getName(), is("M1"));
      verify(heroRepositoryMock).findAll(specificationCaptor.capture(), Mockito.refEq(pageRequest));
      HeroSpecification captorValue = (HeroSpecification) specificationCaptor.getValue();
      SearchCriteria searchCriteria = captorValue.getSearchCriteria();
      assertThat(searchCriteria, notNullValue());
      assertThat(searchCriteria.getKey(), is("name"));
      assertThat(searchCriteria.getOperation(), is(":"));
      assertThat(searchCriteria.getValue(), is("M"));
   }

   @Test
   void shouldSaveHero() {
      ArgumentCaptor<HeroEntity> heroEntityArgumentCaptor = ArgumentCaptor.forClass(HeroEntity.class);
      List<Skill> skills = new ArrayList<>();
      skills.add(new Skill(null, "fly", "can fly"));
      skills.add(new Skill(null, "Walk", "can walk"));
      HeroRequest heroRequest = HeroRequest.builder()
                                           .name("M0")
                                           .skills(skills)
                                           .build();
      SkillEntity skillEntityFly = new SkillEntity(99L, "Fly", "Flying", null);

      when(skillRepositoryMock.findByNameIgnoreCase("fly")).thenReturn(Optional.of(skillEntityFly));
      when(skillRepositoryMock.findByNameIgnoreCase("Walk")).thenReturn(Optional.empty());
      when(heroRepositoryMock.save(any(HeroEntity.class))).thenAnswer(invocationOnMock -> {
         HeroEntity heroEntity = invocationOnMock.getArgument(0);
         heroEntity.setId(1L);
         return heroEntity;
      });

      HeroResponse heroResponse = heroService.save(heroRequest);

      assertThat(heroResponse.getId(), is(1L));
      assertThat(heroResponse.getName(), is("M0"));
      assertThat(heroResponse.getSkills(), hasSize(2));
      assertThat(heroResponse.getSkills().get(0).getId(), is(99L));
      assertThat(heroResponse.getSkills().get(0).getName(), is("Fly"));
      assertThat(heroResponse.getSkills().get(0).getDescription(), is("Flying"));
      assertThat(heroResponse.getSkills().get(1).getId(), nullValue());
      assertThat(heroResponse.getSkills().get(1).getName(), is("Walk"));
      assertThat(heroResponse.getSkills().get(1).getDescription(), is("can walk"));
      verify(skillRepositoryMock).findByNameIgnoreCase("fly");
      verify(skillRepositoryMock).findByNameIgnoreCase("Walk");
      verify(heroRepositoryMock).save(heroEntityArgumentCaptor.capture());
      HeroEntity argumentCaptorValue = heroEntityArgumentCaptor.getValue();
      assertThat(argumentCaptorValue.getId(), is(1L));
      assertThat(argumentCaptorValue.getName(), is("M0"));
      assertThat(argumentCaptorValue.getSkills(), hasSize(2));
      assertThat(argumentCaptorValue.getSkills().get(0).getId(), is(99L));
      assertThat(argumentCaptorValue.getSkills().get(0).getName(), is("Fly"));
      assertThat(argumentCaptorValue.getSkills().get(0).getDescription(), is("Flying"));
      assertThat(argumentCaptorValue.getSkills().get(1).getId(), nullValue());
      assertThat(argumentCaptorValue.getSkills().get(1).getName(), is("Walk"));
      assertThat(argumentCaptorValue.getSkills().get(1).getDescription(), is("can walk"));
   }

   @Test
   void shouldThrowValidationExceptionWhenTryToEditInvalidHeroId() {
      Assertions.assertThrows(ValidationException.class, () -> {
         heroService.editHero(99L, new HeroRequest());
      });

      verify(heroRepositoryMock).findById(99L);
   }

   @Test
   void shouldEditHero() {
      ArgumentCaptor<HeroEntity> heroEntityArgumentCaptor = ArgumentCaptor.forClass(HeroEntity.class);
      HeroEntity heroEntity = HeroEntity.builder().name("Pedro").realName("No-Pedro").build();
      heroEntity.setId(99L);

      when(heroRepositoryMock.findById(99L)).thenReturn(Optional.of(heroEntity));
      when(heroRepositoryMock.save(any(HeroEntity.class)))
            .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

      HeroResponse heroResponse = heroService.editHero(99L, HeroRequest.builder().name("Manolo").build());

      assertThat(heroResponse.getId(), is(99L));
      assertThat(heroResponse.getName(), is("Manolo"));
      assertThat(heroResponse.getRealName(), is("No-Pedro"));
      verify(heroRepositoryMock).findById(99L);
      verify(heroRepositoryMock).save(heroEntityArgumentCaptor.capture());
      HeroEntity argumentCaptorValue = heroEntityArgumentCaptor.getValue();
      assertThat(argumentCaptorValue.getId(), is(99L));
      assertThat(argumentCaptorValue.getName(), is("Manolo"));
      assertThat(argumentCaptorValue.getRealName(), is("No-Pedro"));

   }
}