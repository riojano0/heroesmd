package com.montivero.poc.heroesmd.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class BasicWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

   @Autowired
   private Environment env;

   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      String username = env.getProperty("HEROES-USER");
      String password = env.getProperty("HEROES-PASSWORD");

      if (StringUtils.isNotBlank(username)) {
         auth.inMemoryAuthentication()
             .passwordEncoder(passwordEncoder())
             .withUser(username).password(passwordEncoder().encode(password)).roles("ADMIN");
      }
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http.cors().disable()
          .csrf().disable()
          .authorizeHttpRequests()
          .antMatchers("/h2-console/**").permitAll()
          .antMatchers(HttpMethod.GET, "/**").permitAll()
          .antMatchers(HttpMethod.POST, "/hero", "/hero/**").hasRole("ADMIN")
          .antMatchers(HttpMethod.PUT, "/hero", "/hero/**").hasRole("ADMIN")
          .antMatchers(HttpMethod.DELETE, "/hero", "/hero/**").hasRole("ADMIN")
          .and().httpBasic();

      // Allow to work h2-console
      http.headers().frameOptions().disable();
   }
}
