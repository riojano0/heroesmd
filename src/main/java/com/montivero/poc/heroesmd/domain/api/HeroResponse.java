package com.montivero.poc.heroesmd.domain.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.montivero.poc.heroesmd.domain.api.view.CustomJsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonView(CustomJsonView.All.class)
public class HeroResponse {

   @JsonView(CustomJsonView.Admin.class)
   public Long id;

   public String name;

   public String description;

   public String imageUrl;

   public List<Skill> skills;

   @JsonView(CustomJsonView.Admin.class)
   public String realName;

}
