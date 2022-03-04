package com.montivero.poc.heroesmd.domain.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.montivero.poc.heroesmd.domain.api.view.CustomJsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(CustomJsonView.All.class)
public class DeleteHeroResponse {

   private boolean deleted;

}
