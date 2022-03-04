package com.montivero.poc.heroesmd.domain.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "skill")
public class SkillEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(updatable = false, nullable = false)
   public Long id;

   @Column(nullable = false, unique = true)
   public String name;

   @Column(length = 300)
   public String description;

   @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
   @JsonBackReference // Use to avoid the circular reference
   private List<HeroEntity> heroList;

}
