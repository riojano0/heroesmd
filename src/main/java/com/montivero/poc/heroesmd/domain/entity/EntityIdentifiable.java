package com.montivero.poc.heroesmd.domain.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class EntityIdentifiable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column
   public Long id;

}
