package com.montivero.poc.heroesmd.config;

import java.net.URI;
import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import com.montivero.poc.heroesmd.domain.api.view.CustomJsonView;

@RestControllerAdvice
public class JsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {

   @Override
   protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType,
         ServerHttpRequest request, ServerHttpResponse response) {

      URI uri = request.getURI();
      if (StringUtils.contains( uri.getPath(), "/hero")) {

         SecurityContext context = SecurityContextHolder.getContext();
         Authentication authentication = context.getAuthentication();
         Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

         if (CollectionUtils.isNotEmpty(authorities)) {
            boolean hasAdminAuthority = authorities.stream().anyMatch(auth -> "ROLE_ADMIN".equalsIgnoreCase(auth.getAuthority()));
            if (hasAdminAuthority) {
               bodyContainer.setSerializationView(CustomJsonView.Admin.class);
               return;
            }
         }
         bodyContainer.setSerializationView(CustomJsonView.All.class);
      }

   }
}
