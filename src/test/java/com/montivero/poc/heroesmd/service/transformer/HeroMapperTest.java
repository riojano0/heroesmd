package com.montivero.poc.heroesmd.service.transformer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.montivero.poc.heroesmd.domain.api.HeroRequest;
import com.montivero.poc.heroesmd.domain.api.Skill;
import com.montivero.poc.heroesmd.domain.entity.HeroEntity;
import com.montivero.poc.heroesmd.domain.entity.SkillEntity;

class HeroMapperTest {

   @Test
   void shouldConvertToEntity() {
      HeroRequest heroRequest = HeroRequest
            .builder()
            .name("a")
            .description("b")
            .imageUrl("c")
            .realName("d")
            .skills(Collections.singletonList(new Skill("x", "y")))
            .build();

      HeroEntity heroEntity = HeroMapper.INSTANCE.toEntity(heroRequest);

      assertThat(heroEntity.getId(), nullValue());
      assertThat(heroEntity.getName(), is("a"));
      assertThat(heroEntity.getDescription(), is("b"));
      assertThat(heroEntity.getImageUrl(), is("c"));
      assertThat(heroEntity.getRealName(), is("d"));
      List<SkillEntity> skills = heroEntity.getSkills();
      assertThat(skills, hasSize(1));
      assertThat(skills.get(0).getName(), is("x"));
      assertThat(skills.get(0).getDescription(), is("y"));
   }

}