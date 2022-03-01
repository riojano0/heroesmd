package com.montivero.poc.heroesmd.domain.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "skill")
public class SkillEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column
   public Long id;

   @Column(nullable = false, unique = true)
   public String name;

   @Column(length = 300)
   public String description;

   @ManyToMany(mappedBy = "skills")
   @JsonBackReference // Use to avoid the circular reference
   private List<HeroEntity> heroList;

}
