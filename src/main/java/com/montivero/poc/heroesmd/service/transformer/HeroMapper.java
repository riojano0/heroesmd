package com.montivero.poc.heroesmd.service.transformer;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.montivero.poc.heroesmd.domain.api.HeroRequest;
import com.montivero.poc.heroesmd.domain.api.Skill;
import com.montivero.poc.heroesmd.domain.entity.HeroEntity;
import com.montivero.poc.heroesmd.domain.entity.SkillEntity;

@Mapper
public interface HeroMapper {

   HeroMapper INSTANCE = Mappers.getMapper(HeroMapper.class);

   HeroEntity toEntity(HeroRequest heroRequest);

   List<SkillEntity> toEntity(List<Skill> skill);

}
