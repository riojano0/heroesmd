package com.montivero.poc.heroesmd.domain.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "hero")
public class HeroEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column
   private Long id;

   @Column(nullable = false, unique = true)
   private String name;

   @Column(length = 300)
   private String description;

   @Column(name = "image_url")
   private String imageUrl;

   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Allow to update and persist new Skills
   //Only use to set the column name convention
   @JoinTable(name = "hero_skills", joinColumns = @JoinColumn(name = "hero_id"),
         inverseJoinColumns = @JoinColumn(name = "skill_id"))
   @JsonManagedReference //This must be serialize
   private List<SkillEntity> skills;

   @Column(name = "real_name")
   private String realName;

}
