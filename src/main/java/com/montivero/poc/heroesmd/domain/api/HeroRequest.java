package com.montivero.poc.heroesmd.domain.api;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeroRequest {

   @NotBlank
   private String name;

   private String description;

   private String imageUrl;

   private List<Skill> skills;

   private String realName;

}
