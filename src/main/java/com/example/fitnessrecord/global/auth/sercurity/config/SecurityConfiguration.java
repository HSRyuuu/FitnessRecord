package com.example.fitnessrecord.global.auth.sercurity.config;

import com.example.fitnessrecord.global.auth.sercurity.errorhandler.MyAccessDeniedHandler;
import com.example.fitnessrecord.global.auth.sercurity.errorhandler.MyAuthenticationEntryPoint;
import com.example.fitnessrecord.global.auth.sercurity.filter.JwtAuthenticationFilter;
import com.example.fitnessrecord.global.auth.sercurity.filter.JwtExceptionFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {

  private final Environment environment;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtExceptionFilter jwtExceptionFilter;

  private final MyAccessDeniedHandler myAccessDeniedHandler;
  private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;

  private static final String[] PERMIT_URL = {
      //swagger
      "/v2/api-docs",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui.html",
      "/swagger-ui/**",
      "/webjars/**",
      "/auth/**",
      "/login-page"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.formLogin().disable();

    http
        .authorizeRequests()
        .antMatchers(PERMIT_URL)
        .permitAll()
        .antMatchers("/admin/**")
        .hasAuthority("ROLE_ADMIN")
        .antMatchers("/**")
        .hasAuthority("ROLE_USER");
    http
        .exceptionHandling()
        .authenticationEntryPoint(myAuthenticationEntryPoint)
        .accessDeniedHandler(myAccessDeniedHandler);
    http
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

    if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
      http.authorizeRequests().antMatchers("/**").permitAll();
    }

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().antMatchers( "/login/**", "/exception/**");
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
