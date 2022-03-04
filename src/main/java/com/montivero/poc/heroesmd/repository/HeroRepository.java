package com.montivero.poc.heroesmd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.montivero.poc.heroesmd.domain.entity.HeroEntity;

@Repository
public interface HeroRepository extends PagingAndSortingRepository<HeroEntity, Long>, JpaSpecificationExecutor<HeroEntity> {

   List<HeroEntity> findAll();

   //Using slice to avoid the extra count(*)
   Slice<HeroEntity> findBy(Pageable pageable);

   Optional<HeroEntity> findByName(String name);
}
