package com.montivero.poc.heroesmd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.montivero.poc.heroesmd.domain.entity.SkillEntity;

public interface SkillRepository extends PagingAndSortingRepository<SkillEntity, Long> {

   List<SkillEntity> findAll();

   Optional<SkillEntity> findByNameIgnoreCase(String name);

}
