package com.montivero.poc.heroesmd.domain.api.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorFieldResponse {

   private String field;
   private String message;

}
