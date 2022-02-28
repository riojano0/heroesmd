package com.montivero.poc.heroesmd.repository.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.montivero.poc.heroesmd.domain.entity.HeroEntity;

public class HeroSpecificationBuilder {

   private final List<SearchCriteria> params;

   public HeroSpecificationBuilder() {
      params = new ArrayList<SearchCriteria>();
   }

   public HeroSpecificationBuilder with(String key, String operation, Object value) {
      params.add(new SearchCriteria(key, operation, value));
      return this;
   }
   public Specification<HeroEntity> build() {
      if (params.size() == 0) {
         return null;
      }

      List<Specification<HeroEntity>> specificationList = params.stream()
                                        .map(HeroSpecification::new)
                                        .collect(Collectors.toList());

      Specification<HeroEntity> specificationResult = specificationList.get(0);

      for (int i = 1; i < params.size(); i++) {
         Specification<HeroEntity> nextSpecification = specificationList.get(i);
         specificationResult = Specification.where(specificationResult)
                                            .and(nextSpecification);
      }

      return specificationResult;
   }

}
