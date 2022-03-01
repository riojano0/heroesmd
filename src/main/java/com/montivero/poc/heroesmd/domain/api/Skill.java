package com.montivero.poc.heroesmd.domain.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

   private Long id;
   private String name;
   private String description;

}
