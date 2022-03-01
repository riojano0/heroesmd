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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "hero")
public class HeroEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column
   public Long id;

   @Column(nullable = false, unique = true)
   public String name;

   @Column(length = 300)
   public String description;

   @Column(name = "image_url")
   public String imageUrl;

   @ManyToMany(cascade = CascadeType.PERSIST)
   //Only use to set the column name convention
   @JoinTable(name = "hero_skills", joinColumns = @JoinColumn(name = "hero_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
   @JsonManagedReference //This must be serialize
   public List<SkillEntity> skills;

   @Column(name = "real_name")
   public String realName;

}
