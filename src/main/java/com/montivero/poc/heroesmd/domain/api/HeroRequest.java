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
   public String name;

   public String description;

   public String imageUrl;

   public List<Skill> skills;

   public String realName;

}
