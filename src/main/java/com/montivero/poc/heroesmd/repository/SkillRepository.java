package com.montivero.poc.heroesmd.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.montivero.poc.heroesmd.domain.entity.SkillEntity;

public interface SkillRepository extends PagingAndSortingRepository<SkillEntity, Long> {

   Optional<SkillEntity> findByNameIgnoreCase(String name);

}
