package com.montivero.poc.heroesmd.domain.api;

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
@JsonView(CustomJsonView.All.class)
public class Skill {

   @JsonView(CustomJsonView.Admin.class)
   private Long id;

   private String name;
   private String description;

}
