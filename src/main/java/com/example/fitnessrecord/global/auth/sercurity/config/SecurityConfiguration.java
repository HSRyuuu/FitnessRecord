package com.example.fitnessrecord.global.auth.sercurity.config;

import com.example.fitnessrecord.global.auth.sercurity.errorhandler.MyAccessDeniedHandler;
import com.example.fitnessrecord.global.auth.sercurity.errorhandler.MyAuthenticationEntryPoint;
import com.example.fitnessrecord.global.auth.sercurity.filter.AuthenticationFilter;
import com.example.fitnessrecord.global.auth.sercurity.filter.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

  private final AuthenticationFilter authenticationFilter;
  private final JwtExceptionFilter jwtExceptionFilter;

  private final MyAccessDeniedHandler myAccessDeniedHandler;
  private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/admin/**")
        .hasAuthority("ROLE_ADMIN")
        .and()
        .authorizeRequests()
        .antMatchers("/user/**")
        .hasAuthority("ROLE_USER")
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(myAuthenticationEntryPoint)
        .accessDeniedHandler(myAccessDeniedHandler)
        .and()
        .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtExceptionFilter, AuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().antMatchers("/register/**", "/login/**", "/exception/**");
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
