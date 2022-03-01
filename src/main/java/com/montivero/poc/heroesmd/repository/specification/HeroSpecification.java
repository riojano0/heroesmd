package com.montivero.poc.heroesmd.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.montivero.poc.heroesmd.domain.entity.HeroEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HeroSpecification implements Specification<HeroEntity> {

   private final SearchCriteria searchCriteria;

   @Override
   public Predicate toPredicate(Root<HeroEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
      if (searchCriteria != null) {
         String key = searchCriteria.getKey();
         Object value = searchCriteria.getValue();
         Path<String> expression = root.get(key);

         if (searchCriteria.getOperation().equalsIgnoreCase(">")) {
            return criteriaBuilder.greaterThanOrEqualTo(expression, value.toString());
         } else if (searchCriteria.getOperation().equalsIgnoreCase("<")) {
            return criteriaBuilder.lessThanOrEqualTo(expression, value.toString());
         } else if (searchCriteria.getOperation().equalsIgnoreCase(":")) {
            if (expression.getJavaType() == String.class) {
               Expression<String> stringExpressionLower = criteriaBuilder.lower(expression);
               String lowerCaseValue = StringUtils.lowerCase((String) value);
               return criteriaBuilder.like(stringExpressionLower, "%" + lowerCaseValue + "%");
            } else {
               return criteriaBuilder.equal(expression, value);
            }
         }
      }

      return null;
   }

   public SearchCriteria getSearchCriteria() {
      return searchCriteria;
   }
}
