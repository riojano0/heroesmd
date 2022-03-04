package com.montivero.poc.heroesmd.service.transformer;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.montivero.poc.heroesmd.domain.api.HeroRequest;
import com.montivero.poc.heroesmd.domain.api.HeroResponse;
import com.montivero.poc.heroesmd.domain.api.Skill;
import com.montivero.poc.heroesmd.domain.entity.HeroEntity;
import com.montivero.poc.heroesmd.domain.entity.SkillEntity;

// Remove all MapStruct dependencies.
@Mapper
public interface HeroMapper {

   HeroMapper INSTANCE = Mappers.getMapper(HeroMapper.class);

   default HeroEntity toEntity(HeroRequest heroRequest) {
      if (heroRequest == null) {
         return null;
      }

      HeroEntity heroEntity = new HeroEntity();
      heroEntity.setName(heroRequest.getName());
      heroEntity.setDescription(heroRequest.getDescription());
      heroEntity.setImageUrl(heroRequest.getImageUrl());
      List<Skill> skills = heroRequest.getSkills();
      List<SkillEntity> skillEntities = toSkillEntityList(skills);
      heroEntity.setSkills(skillEntities);
      heroEntity.setRealName(heroRequest.getRealName());

      return heroEntity;
   };

   default HeroResponse toResponse(HeroEntity heroEntity) {
      if (heroEntity == null) {
         return null;
      }

      HeroResponse heroResponse = new HeroResponse();
      heroResponse.setName(heroEntity.getName());
      heroResponse.setDescription(heroEntity.getDescription());
      heroResponse.setImageUrl(heroEntity.getImageUrl());
      List<SkillEntity> skills = heroEntity.getSkills();
      List<Skill> skillList = toSkillResponseList(skills);
      heroResponse.setSkills(skillList);
      heroResponse.setRealName(heroEntity.getRealName());

      return heroResponse;
   };

   // Need for inner deserialization
   List<SkillEntity> toSkillEntityList(List<Skill> skill);

   // Give implementation to avoid issue on library
   default SkillEntity toSkillEntity(Skill skill) {
      if ( skill == null ) {
         return null;
      }

      SkillEntity skillEntity = new SkillEntity();
      skillEntity.setId(skill.getId());
      skillEntity.setName(skill.getName());
      skillEntity.setDescription(skill.getDescription());

      return skillEntity;
   };

   // Need for inner deserialization
   List<Skill> toSkillResponseList(List<SkillEntity> skillEntities);

   // Give implementation to avoid issue on library
   default Skill toSkillResponse(SkillEntity skillEntity) {
      if ( skillEntity == null ) {
         return null;
      }

      Skill skill = new Skill();
      skill.setId(skillEntity.getId());
      skill.setName(skillEntity.getName());
      skill.setDescription(skillEntity.getDescription());

      return skill;
   };
}
