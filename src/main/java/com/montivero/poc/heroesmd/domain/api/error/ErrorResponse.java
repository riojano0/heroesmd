package com.montivero.poc.heroesmd.domain.api.error;

import java.time.LocalDateTime;
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
public class ErrorResponse {

   private LocalDateTime date;
   private String path;
   private String message;
   private String detail;
   private String internalCode;
   private List<ErrorFieldResponse> fields;

}
