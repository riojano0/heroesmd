package com.montivero.poc.heroesmd.domain.api.view;

import java.util.HashMap;
import java.util.Map;

public class CustomJsonView {

   public static final Map<String, Class> MAPPING = new HashMap<>();

   static {
      MAPPING.put("ADMIN", Admin.class);
   }

   public interface All {}
   public interface Admin extends All {}

}
