package com.montivero.poc.heroesmd.domain.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HeroResponse {

   public Long id;

   public String name;

   public String description;

   public String imageUrl;

   public List<Skill> skills;

   public String realName;

}
