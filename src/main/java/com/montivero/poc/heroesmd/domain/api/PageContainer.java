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
public class PageContainer<T> {

   private List<T> content;
   private boolean lastPage;
   private int page;
   private int size;

}
