package com.example.fitnessrecord.global.auth.sercurity.config;

import com.example.fitnessrecord.global.auth.sercurity.errorhandler.MyAccessDeniedHandler;
import com.example.fitnessrecord.global.auth.sercurity.errorhandler.MyAuthenticationEntryPoint;
import com.example.fitnessrecord.global.auth.sercurity.filter.AuthenticationFilter;
import com.example.fitnessrecord.global.auth.sercurity.filter.JwtExceptionFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final Environment environment;
  private final AuthenticationFilter authenticationFilter;
  private final JwtExceptionFilter jwtExceptionFilter;

  private final MyAccessDeniedHandler myAccessDeniedHandler;
  private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;

  @Override
  public void configure(HttpSecurity http) throws Exception {

    http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    //admin 페이지 권한
    http
        .authorizeRequests()
        .antMatchers("/admin/**")
        .hasAuthority("ROLE_ADMIN");

    //예외 핸들링
    http
        .exceptionHandling()
        .authenticationEntryPoint(myAuthenticationEntryPoint)
        .accessDeniedHandler(myAccessDeniedHandler);
    http
        .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtExceptionFilter, AuthenticationFilter.class);

    // 개발용
    if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
      http.authorizeRequests().antMatchers("/**").permitAll();
    }

  }

  @Override
  public void configure(final WebSecurity webSecurity) {
    webSecurity.ignoring()
        .antMatchers("/register/**", "/login/**", "/exception/**");
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
