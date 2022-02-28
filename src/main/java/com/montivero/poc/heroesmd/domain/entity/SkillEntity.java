package com.montivero.poc.heroesmd.domain.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "skill")
public class SkillEntity extends EntityIdentifiable {

   @Column(nullable = false, unique = true)
   public String name;

   @Column(length = 300)
   public String description;

   @ManyToMany(mappedBy = "skills")
   @JsonBackReference // Use to avoid the circular reference
   private List<HeroEntity> heroList;

}
