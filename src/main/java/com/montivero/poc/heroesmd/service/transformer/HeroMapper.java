package com.montivero.poc.heroesmd.service.transformer;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.montivero.poc.heroesmd.domain.api.HeroRequest;
import com.montivero.poc.heroesmd.domain.api.HeroResponse;
import com.montivero.poc.heroesmd.domain.api.Skill;
import com.montivero.poc.heroesmd.domain.entity.HeroEntity;
import com.montivero.poc.heroesmd.domain.entity.SkillEntity;

@Mapper
public interface HeroMapper {

   HeroMapper INSTANCE = Mappers.getMapper(HeroMapper.class);

   HeroEntity toEntity(HeroRequest heroRequest);

   HeroResponse toResponse(HeroEntity heroEntity);

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
