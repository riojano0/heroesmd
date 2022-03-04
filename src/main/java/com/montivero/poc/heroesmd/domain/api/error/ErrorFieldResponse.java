package com.montivero.poc.heroesmd.domain.api.error;

import com.fasterxml.jackson.annotation.JsonView;
import com.montivero.poc.heroesmd.domain.api.view.CustomJsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(CustomJsonView.All.class)
public class ErrorFieldResponse {

   private String field;
   private String message;

}
